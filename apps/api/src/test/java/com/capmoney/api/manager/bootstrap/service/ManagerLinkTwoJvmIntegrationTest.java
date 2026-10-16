package com.capmoney.api.manager.bootstrap.service;

import static org.junit.jupiter.api.Assertions.*;
import com.capmoney.api.manager.bootstrap.repository.BootstrapStore;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.MessageDigest;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;

/** Two separate app JVMs race against one retained PostgreSQL, Redis and Mailpit. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = "spring.config.on-not-found=ignore")
@EnabledIfEnvironmentVariable(named = "CAPMONEY_CONTAINER_TESTS", matches = "true")
class ManagerLinkTwoJvmIntegrationTest {
    static PostgreSQLContainer postgres;
    static GenericContainer<?> redis;
    static GenericContainer<?> smtp;
    static Path edge;
    @Autowired BootstrapStore store;
    @Autowired JdbcTemplate jdbc;

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
        edge = Files.createTempFile("capmoney-cm02-two-jvm-edge-", ".test");
        Files.writeString(edge, "e".repeat(64));
        Files.setPosixFilePermissions(edge, PosixFilePermissions.fromString("rw-------"));
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
        registry.add("spring.mail.host", smtp::getHost);
        registry.add("spring.mail.port", () -> smtp.getMappedPort(1025));
        registry.add("capmoney.origin", () -> "http://127.0.0.1:28080");
        registry.add("capmoney.api-subnet", () -> "127.0.0.0/8");
        registry.add("capmoney.edge-secret-file", edge::toString);
        registry.add("capmoney.jwt-key-file", edge::toString);
        registry.add("capmoney.jwt-lookup-key-file", edge::toString);
        registry.add("capmoney.bootstrap.enabled", () -> false);
        registry.add("capmoney.bootstrap.email", () -> "");
    }

    @Test
    void exactlyOneProcessRenewsAndOnlyItsEmailTokenIsValid() throws Exception {
        String proof = "p".repeat(43);
        var initial = store.reserve("manager@example.test", digest(proof));
        assertNotNull(initial);
        assertTrue(store.issue(initial, Instant.now(), Clock.systemUTC()));
        jdbc.update("WITH stamp AS (SELECT clock_timestamp() - interval '72 hours 1 second' AS t) "
                + "UPDATE manager_bootstrap SET accepted_at=stamp.t, expires_at=stamp.t + interval '72 hours' FROM stamp");
        assertEquals(BootstrapStore.LinkStatus.expired, store.linkStatus(digest(proof)));

        Path directory = Files.createTempDirectory("capmoney-cm02-two-jvm-");
        Files.setPosixFilePermissions(directory, PosixFilePermissions.fromString("rwx------"));
        Path config = directory.resolve("worker.properties");
        Properties properties = new Properties();
        properties.setProperty("spring.datasource.url", postgres.getJdbcUrl());
        properties.setProperty("spring.datasource.username", postgres.getUsername());
        properties.setProperty("spring.datasource.password", postgres.getPassword());
        properties.setProperty("spring.data.redis.host", redis.getHost());
        properties.setProperty("spring.data.redis.port", redis.getMappedPort(6379).toString());
        properties.setProperty("spring.mail.host", smtp.getHost());
        properties.setProperty("spring.mail.port", smtp.getMappedPort(1025).toString());
        properties.setProperty("capmoney.origin", "http://127.0.0.1:28080");
        properties.setProperty("capmoney.api-subnet", "127.0.0.0/8");
        properties.setProperty("capmoney.edge-secret-file", edge.toString());
        properties.setProperty("capmoney.jwt-key-file", edge.toString());
        properties.setProperty("capmoney.jwt-lookup-key-file", edge.toString());
        properties.setProperty("capmoney.bootstrap.enabled", "false");
        properties.setProperty("capmoney.bootstrap.email", "");
        properties.setProperty("capmoney.test.proof-token", proof);
        properties.setProperty("capmoney.test.client-ip", "192.0.2.50");
        try (var output = Files.newBufferedWriter(config)) { properties.store(output, "isolated test only"); }
        Files.setPosixFilePermissions(config, PosixFilePermissions.fromString("rw-------"));

        Path start = directory.resolve("start");
        Process first = worker(config, directory.resolve("first.ready"), start, directory.resolve("first.log"));
        Process second = worker(config, directory.resolve("second.ready"), start, directory.resolve("second.log"));
        long deadline = System.nanoTime() + Duration.ofSeconds(60).toNanos();
        while ((!Files.exists(directory.resolve("first.ready")) || !Files.exists(directory.resolve("second.ready")))
                && System.nanoTime() < deadline && first.isAlive() && second.isAlive()) Thread.sleep(50);
        assertTrue(first.isAlive() && second.isAlive(), "worker exited before barrier; inspect retained logs");
        assertTrue(Files.exists(directory.resolve("first.ready")) && Files.exists(directory.resolve("second.ready")),
                "workers did not reach barrier; inspect retained logs");
        Files.writeString(start, "go");
        assertTrue(first.waitFor(60, TimeUnit.SECONDS), "first JVM did not finish");
        assertTrue(second.waitFor(60, TimeUnit.SECONDS), "second JVM did not finish");
        assertEquals(0, first.exitValue(), Files.readString(directory.resolve("first.log")));
        assertEquals(0, second.exitValue(), Files.readString(directory.resolve("second.log")));
        String firstLog = Files.readString(directory.resolve("first.log"));
        String secondLog = Files.readString(directory.resolve("second.log"));
        assertEquals(Set.of("accepted", "SETUP_LINK_UNAVAILABLE"),
                Set.of(result(firstLog), result(secondLog)));
        assertEquals("issued", jdbc.queryForObject("SELECT state FROM manager_bootstrap", String.class));
        assertEquals(2L, jdbc.queryForObject("SELECT generation FROM manager_bootstrap", Long.class));
        assertEquals(BootstrapStore.LinkStatus.unavailable, store.linkStatus(digest(proof)));
        assertEquals(1, mailCount());
        String message = mailMessage();
        var tokens = Pattern.compile("#token=([A-Za-z0-9_-]{43})").matcher(message);
        assertTrue(tokens.find());
        assertEquals(digest(tokens.group(1)), jdbc.queryForObject("SELECT token_digest FROM manager_bootstrap", String.class));
        assertEquals(BootstrapStore.LinkStatus.active, store.linkStatus(digest(tokens.group(1))));
    }

    private Process worker(Path config, Path ready, Path start, Path log) throws Exception {
        String java = Path.of(System.getProperty("java.home"), "bin", "java").toString();
        return new ProcessBuilder(java, "-cp", System.getProperty("java.class.path"),
                ManagerLinkRenewalWorker.class.getName(), config.toString(), ready.toString(), start.toString())
                .redirectErrorStream(true).redirectOutput(log.toFile()).start();
    }
    private String result(String log) {
        var match = Pattern.compile("RENEW_RESULT=([A-Z_]+|accepted)").matcher(log);
        assertTrue(match.find(), "worker result missing; inspect retained log");
        return match.group(1);
    }
    private String mailMessage() throws Exception {
        var listing = mailListing();
        var ids = Pattern.compile("\"ID\"\\s*:\\s*\"([^\"]+)\"").matcher(listing);
        assertTrue(ids.find());
        var response = HttpClient.newHttpClient().send(HttpRequest.newBuilder(mailpit("/api/v1/message/" + ids.group(1)))
                .GET().build(), HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        return response.body();
    }
    private int mailCount() throws Exception {
        var ids = Pattern.compile("\"ID\"\\s*:\\s*\"([^\"]+)\"").matcher(mailListing());
        int count = 0;
        while (ids.find()) count++;
        return count;
    }
    private String mailListing() throws Exception {
        var response = HttpClient.newHttpClient().send(HttpRequest.newBuilder(mailpit("/api/v1/messages"))
                .GET().build(), HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        return response.body();
    }
    private URI mailpit(String path) { return URI.create("http://" + smtp.getHost() + ":" + smtp.getMappedPort(8025) + path); }
    private String digest(String token) throws Exception {
        return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                .digest(token.getBytes(StandardCharsets.US_ASCII)));
    }
}
