package com.capmoney.api.manager.bootstrap.service;

import static org.junit.jupiter.api.Assertions.*;
import com.capmoney.api.http.ApiProblem;
import com.capmoney.api.manager.bootstrap.repository.BootstrapStore;
import com.capmoney.api.manager.setup.repository.ManagerSetupStore;
import java.net.ConnectException;
import java.net.URI;
import java.net.SocketTimeoutException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.MessageDigest;
import java.time.Clock;
import java.time.Instant;
import java.util.HexFormat;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;

/** Isolated retained database and Redis. No project data is reset. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.config.on-not-found=ignore", "capmoney.security.cookie-secure=false"})
@ActiveProfiles("test")
@EnabledIfEnvironmentVariable(named = "CAPMONEY_CONTAINER_TESTS", matches = "true")
class ManagerLinkRenewalIntegrationTest {
    static final String EMAIL = "manager@example.test";
    static PostgreSQLContainer postgres;
    static GenericContainer<?> redis;
    @Autowired BootstrapStore store;
    @Autowired ManagerSetupStore setup;
    @Autowired JdbcTemplate jdbc;
    @Autowired ManagerLinkThrottle throttle;
    @LocalServerPort int port;
    final HttpClient client = HttpClient.newHttpClient();

    @DynamicPropertySource
    static void dependencies(DynamicPropertyRegistry registry) throws Exception {
        assertEquals("true", System.getenv("TESTCONTAINERS_RYUK_DISABLED"));
        assertEquals("true", System.getenv("TESTCONTAINERS_REUSE_ENABLE"));
        postgres = new PostgreSQLContainer("postgres:17.7-alpine3.22")
                .withPassword(UUID.randomUUID().toString()).withReuse(true);
        redis = new GenericContainer<>("redis:7.4.6-alpine3.21").withExposedPorts(6379)
                .withEnv("CAPMONEY_TEST_RUN", UUID.randomUUID().toString()).withReuse(true);
        postgres.start(); redis.start();
        Path edge = Files.createTempFile("capmoney-cm02-edge-", ".test");
        Files.writeString(edge, "a".repeat(64));
        Files.setPosixFilePermissions(edge, PosixFilePermissions.fromString("rw-------"));
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
        registry.add("capmoney.origin", () -> "http://127.0.0.1:28080");
        registry.add("capmoney.api-subnet", () -> "127.0.0.0/8");
        registry.add("capmoney.edge-secret-file", edge::toString);
        registry.add("capmoney.jwt-key-file", edge::toString);
        registry.add("capmoney.jwt-lookup-key-file", edge::toString);
    }

    @Test
    void expiredLinkRetriesDeliveryAndOnlyNewestGenerationCanCreateManager() throws Exception {
        String oldToken = "a".repeat(43);
        var initial = store.reserve(EMAIL, digest(oldToken));
        assertNotNull(initial);
        assertTrue(store.issue(initial, Instant.now(), Clock.systemUTC()));
        assertEquals(BootstrapStore.LinkStatus.active, store.linkStatus(digest(oldToken)));
        jdbc.update("WITH stamp AS (SELECT clock_timestamp() - interval '72 hours 1 second' AS t) "
                + "UPDATE manager_bootstrap SET accepted_at=stamp.t, expires_at=stamp.t + interval '72 hours' FROM stamp");
        assertEquals(BootstrapStore.LinkStatus.expired, store.linkStatus(digest(oldToken)));
        var csrf = client.send(request("/api/auth/csrf").GET().build(), HttpResponse.BodyHandlers.ofString());
        String cookie = csrf.headers().allValues("set-cookie").stream()
                .filter(value -> value.startsWith("XSRF-TOKEN=")).findFirst().orElseThrow().split(";", 2)[0];
        var withoutCsrf = client.send(request("/api/manager/setup-link/renew")
                .header("Origin", "http://127.0.0.1:28080").header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"token\":\"" + oldToken + "\"}" )).build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(403, withoutCsrf.statusCode());
        var statusResponse = client.send(request("/api/manager/setup-link/status")
                .header("Origin", "http://127.0.0.1:28080").header("Content-Type", "application/json")
                .header("Cookie", cookie).header("X-XSRF-TOKEN", cookie.substring("XSRF-TOKEN=".length()))
                .POST(HttpRequest.BodyPublishers.ofString("{\"token\":\"" + oldToken + "\"}" )).build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, statusResponse.statusCode());
        assertTrue(statusResponse.body().contains("expired"));
        assertEquals("SETUP_LINK_UNAVAILABLE", assertThrows(ApiProblem.class,
                () -> setup.complete(digest(oldToken), "hash")).code());

        var refused = sender(new MailSendException("refused", new ConnectException()), null);
        assertEquals("DELIVERY_UNCONFIRMED", assertThrows(ApiProblem.class,
                () -> service(refused).renew(oldToken, "192.0.2.1")).code());
        assertEquals("failed", state());
        assertEquals(BootstrapStore.LinkStatus.retryable, store.linkStatus(digest(oldToken)));
        assertEquals("RATE_LIMITED", assertThrows(ApiProblem.class,
                () -> service(refused).renew(oldToken, "192.0.2.1")).code());

        advanceCooldown();
        var timeout = sender(new MailSendException("timeout", new SocketTimeoutException()), null);
        assertEquals("DELIVERY_UNCONFIRMED", assertThrows(ApiProblem.class,
                () -> service(timeout).renew(oldToken, "192.0.2.2")).code());
        assertEquals("unknown", state());
        assertEquals(BootstrapStore.LinkStatus.retryable, store.linkStatus(digest(oldToken)));

        advanceCooldown();
        AtomicReference<String> delivered = new AtomicReference<>();
        ManagerLinkService accepted = service(sender(null, delivered));
        try (var workers = Executors.newFixedThreadPool(2)) {
            var start = new CountDownLatch(1);
            var first = workers.submit(() -> renewCode(accepted, oldToken, "192.0.2.3", start));
            var second = workers.submit(() -> renewCode(accepted, oldToken, "192.0.2.4", start));
            start.countDown();
            assertEquals(java.util.Set.of("accepted", "SETUP_LINK_UNAVAILABLE"),
                    java.util.Set.of(first.get(20, TimeUnit.SECONDS), second.get(20, TimeUnit.SECONDS)));
        }
        assertEquals("issued", state());
        assertEquals(BootstrapStore.LinkStatus.unavailable, store.linkStatus(digest(oldToken)));
        String mail = delivered.get();
        assertNotNull(mail);
        assertTrue(mail.contains("/manager/setup#token="));
        String newToken = mail.split("#token=", 2)[1].substring(0, 43);
        assertEquals(BootstrapStore.LinkStatus.active, store.linkStatus(digest(newToken)));
        assertEquals("SETUP_LINK_UNAVAILABLE", assertThrows(ApiProblem.class,
                () -> setup.complete(digest(oldToken), "hash")).code());
        setup.complete(digest(newToken), "hash");
        assertEquals(1L, jdbc.queryForObject("SELECT count(*) FROM manager_account", Long.class));
        assertEquals("consumed", state());
        assertNull(jdbc.queryForObject("SELECT renewal_proof_digest FROM manager_bootstrap", String.class));
        assertEquals(BootstrapStore.LinkStatus.unavailable, store.linkStatus(digest(newToken)));
        assertEquals("SETUP_LINK_UNAVAILABLE", assertThrows(ApiProblem.class,
                () -> accepted.renew(oldToken, "192.0.2.5")).code());
    }

    private ManagerLinkService service(JavaMailSenderImpl mail) {
        return new ManagerLinkService(store, new BootstrapIssuer(store, mail, Clock.systemUTC(), false,
                "", "http://127.0.0.1:28080"), throttle);
    }
    private JavaMailSenderImpl sender(MailSendException failure, AtomicReference<String> delivered) {
        return new JavaMailSenderImpl() {
            @Override public void send(SimpleMailMessage message) {
                if (failure != null) throw failure;
                delivered.set(message.getText());
            }
        };
    }
    private String renewCode(ManagerLinkService service, String token, String ip, CountDownLatch start) {
        try { start.await(); service.renew(token, ip); return "accepted"; }
        catch (ApiProblem expected) { return expected.code(); }
        catch (InterruptedException interrupted) { Thread.currentThread().interrupt(); throw new RuntimeException(interrupted); }
    }
    private void advanceCooldown() {
        jdbc.update("UPDATE manager_bootstrap SET renewal_reserved_at=clock_timestamp()-interval '16 minutes'");
    }
    private String state() { return jdbc.queryForObject("SELECT state FROM manager_bootstrap", String.class); }
    private String digest(String token) throws Exception {
        return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                .digest(token.getBytes(StandardCharsets.US_ASCII)));
    }
    private HttpRequest.Builder request(String path) {
        return HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + path))
                .header("X-CM-Edge-Auth", "a".repeat(64))
                .header("X-Forwarded-For", "192.0.2.99").header("X-Real-IP", "192.0.2.99");
    }
}
