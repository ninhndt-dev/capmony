package com.capmoney.api.manager.bootstrap.service;

import static org.junit.jupiter.api.Assertions.*;
import com.capmoney.api.manager.bootstrap.repository.BootstrapStore;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.MessageDigest;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import java.util.UUID;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;

/** Real HTTP, PostgreSQL, Redis and SMTP/Mailpit on newly created retained test resources. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.config.on-not-found=ignore", "capmoney.security.cookie-secure=false"})
@ActiveProfiles("test")
@EnabledIfEnvironmentVariable(named = "CAPMONEY_CONTAINER_TESTS", matches = "true")
class ManagerLinkMailpitIntegrationTest {
    static final String EMAIL = "manager@example.test";
    static final String ORIGIN = "http://127.0.0.1:28080";
    static final String EDGE = "e".repeat(64);
    static PostgreSQLContainer postgres;
    static GenericContainer<?> redis;
    static GenericContainer<?> smtp;
    @LocalServerPort int port;
    @Autowired BootstrapStore store;
    @Autowired JdbcTemplate jdbc;
    final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3)).build();

    @DynamicPropertySource
    static void dependencies(DynamicPropertyRegistry registry) throws Exception {
        assertEquals("true", System.getenv("TESTCONTAINERS_RYUK_DISABLED"));
        assertEquals("true", System.getenv("TESTCONTAINERS_REUSE_ENABLE"));
        postgres = new PostgreSQLContainer("postgres:17.7-alpine3.22")
                .withPassword(UUID.randomUUID().toString()).withReuse(true);
        redis = new GenericContainer<>("redis:7.4.6-alpine3.21").withExposedPorts(6379)
                .withEnv("CAPMONEY_TEST_RUN", UUID.randomUUID().toString()).withReuse(true);
        smtp = new GenericContainer<>("axllent/mailpit:v1.27.4").withExposedPorts(1025, 8025)
                .withEnv("CAPMONEY_TEST_RUN", UUID.randomUUID().toString()).withReuse(true);
        postgres.start(); redis.start(); smtp.start();
        Path edge = Files.createTempFile("capmoney-cm02-mailpit-edge-", ".test");
        Files.writeString(edge, EDGE);
        Files.setPosixFilePermissions(edge, PosixFilePermissions.fromString("rw-------"));
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
        registry.add("spring.mail.host", smtp::getHost);
        registry.add("spring.mail.port", () -> smtp.getMappedPort(1025));
        registry.add("capmoney.origin", () -> ORIGIN);
        registry.add("capmoney.api-subnet", () -> "127.0.0.0/8");
        registry.add("capmoney.edge-secret-file", edge::toString);
        registry.add("capmoney.jwt-key-file", edge::toString);
        registry.add("capmoney.jwt-lookup-key-file", edge::toString);
        registry.add("capmoney.bootstrap.enabled", () -> false);
        registry.add("capmoney.bootstrap.email", () -> "");
    }

    @Test
    void expiredLinkRenewsThroughRealSmtpAndNewLinkCompletesOnlyManager() throws Exception {
        String oldToken = "m".repeat(43);
        var initial = store.reserve(EMAIL, digest(oldToken));
        assertNotNull(initial);
        assertTrue(store.issue(initial, Instant.now(), Clock.systemUTC()));
        jdbc.update("WITH stamp AS (SELECT clock_timestamp() - interval '72 hours 1 second' AS t) "
                + "UPDATE manager_bootstrap SET accepted_at=stamp.t, expires_at=stamp.t + interval '72 hours' FROM stamp");

        var status = post("/api/manager/setup-link/status", "{\"token\":\"" + oldToken + "\"}", "192.0.2.31");
        assertEquals(200, status.statusCode());
        assertTrue(status.body().contains("expired"));
        assertEquals(204, post("/api/manager/setup-link/renew", "{\"token\":\"" + oldToken + "\"}",
                "192.0.2.32").statusCode());
        assertEquals("issued", jdbc.queryForObject("SELECT state FROM manager_bootstrap", String.class));
        assertEquals("manager@example.test", jdbc.queryForObject("SELECT email FROM manager_bootstrap", String.class));
        assertEquals(259200L, jdbc.queryForObject(
                "SELECT extract(epoch FROM expires_at - accepted_at)::bigint FROM manager_bootstrap", Long.class));

        String message = receivedMessage();
        assertTrue(message.contains(EMAIL));
        var match = Pattern.compile("#token=([A-Za-z0-9_-]{43})").matcher(message);
        assertTrue(match.find(), "Mailpit must contain the renewed setup URL");
        String newToken = match.group(1);
        assertNotEquals(oldToken, newToken);
        assertEquals(digest(newToken), jdbc.queryForObject("SELECT token_digest FROM manager_bootstrap", String.class));
        assertEquals(409, setup(oldToken, "192.0.2.33").statusCode());
        assertEquals(204, setup(newToken, "192.0.2.34").statusCode());
        assertEquals(1L, jdbc.queryForObject("SELECT count(*) FROM manager_account", Long.class));
        assertEquals(409, post("/api/manager/setup-link/renew", "{\"token\":\"" + oldToken + "\"}",
                "192.0.2.35").statusCode());
        assertEquals(1, messageCount());
    }

    private HttpResponse<String> setup(String token, String ip) throws Exception {
        return post("/api/manager/setup", "{\"token\":\"" + token
                + "\",\"password\":\"correct-password\",\"passwordConfirmation\":\"correct-password\"}", ip);
    }
    private HttpResponse<String> post(String path, String json, String ip) throws Exception {
        var csrf = client.send(request("/api/auth/csrf", ip).GET().build(), HttpResponse.BodyHandlers.ofString());
        assertEquals(204, csrf.statusCode());
        String cookie = csrf.headers().allValues("set-cookie").stream()
                .filter(value -> value.startsWith("XSRF-TOKEN=")).findFirst().orElseThrow().split(";", 2)[0];
        return client.send(request(path, ip).header("Origin", ORIGIN).header("Content-Type", "application/json")
                .header("Cookie", cookie).header("X-XSRF-TOKEN", cookie.substring("XSRF-TOKEN=".length()))
                .POST(HttpRequest.BodyPublishers.ofString(json)).build(), HttpResponse.BodyHandlers.ofString());
    }
    private HttpRequest.Builder request(String path, String ip) {
        return HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + path)).timeout(Duration.ofSeconds(15))
                .header("X-CM-Edge-Auth", EDGE).header("X-Forwarded-For", ip).header("X-Real-IP", ip);
    }
    private String receivedMessage() throws Exception {
        for (int attempt = 0; attempt < 30; attempt++) {
            var listing = client.send(HttpRequest.newBuilder(mailpitUri("/api/v1/messages")).GET().build(),
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, listing.statusCode());
            var ids = Pattern.compile("\"ID\"\\s*:\\s*\"([^\"]+)\"").matcher(listing.body());
            if (ids.find()) {
                var message = client.send(HttpRequest.newBuilder(mailpitUri("/api/v1/message/" + ids.group(1)))
                        .GET().build(), HttpResponse.BodyHandlers.ofString());
                assertEquals(200, message.statusCode());
                return message.body();
            }
            Thread.sleep(200);
        }
        fail("Mailpit did not receive the renewed link");
        return "";
    }
    private int messageCount() throws Exception {
        var listing = client.send(HttpRequest.newBuilder(mailpitUri("/api/v1/messages")).GET().build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, listing.statusCode());
        var ids = Pattern.compile("\"ID\"\\s*:\\s*\"([^\"]+)\"").matcher(listing.body());
        int count = 0;
        while (ids.find()) count++;
        return count;
    }
    private URI mailpitUri(String path) { return URI.create("http://" + smtp.getHost() + ":" + smtp.getMappedPort(8025) + path); }
    private String digest(String token) throws Exception {
        return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                .digest(token.getBytes(StandardCharsets.US_ASCII)));
    }
}
