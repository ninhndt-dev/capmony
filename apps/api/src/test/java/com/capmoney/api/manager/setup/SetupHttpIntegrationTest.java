package com.capmoney.api.manager.setup;

import static org.junit.jupiter.api.Assertions.*;
import java.net.*;
import java.net.http.*;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {"spring.config.on-not-found=ignore", "capmoney.security.cookie-secure=false"})
@ActiveProfiles("test")
@EnabledIfEnvironmentVariable(named = "CAPMONEY_CONTAINER_TESTS", matches = "true")
class SetupHttpIntegrationTest {
    @LocalServerPort int port;
    @Autowired JdbcTemplate jdbc;
    @Autowired org.springframework.data.redis.core.StringRedisTemplate redisTemplate;
    static final String EDGE = "c".repeat(64);
    static final String ORIGIN = "http://127.0.0.1:28080";
    static PostgreSQLContainer postgres;
    static GenericContainer<?> redis;
    final HttpClient client = HttpClient.newBuilder().connectTimeout(java.time.Duration.ofSeconds(2)).build();
    @DynamicPropertySource
    static void dependencies(DynamicPropertyRegistry registry) throws Exception {
        assertEquals("true", System.getenv("TESTCONTAINERS_RYUK_DISABLED"));
        assertEquals("true", System.getenv("TESTCONTAINERS_REUSE_ENABLE"));
        postgres = new PostgreSQLContainer("postgres:17.7-alpine3.22").withPassword(UUID.randomUUID().toString()).withReuse(true);
        redis = new GenericContainer<>("redis:7.4.6-alpine3.21").withExposedPorts(6379)
            .withEnv("CAPMONEY_TEST_RUN", UUID.randomUUID().toString()).withReuse(true);
        postgres.start(); redis.start();
        Path edge = Files.createTempFile("capmoney-setup-edge-", ".test");
        Files.setPosixFilePermissions(edge, PosixFilePermissions.fromString("rw-------")); Files.writeString(edge, EDGE);
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
        registry.add("capmoney.origin", () -> ORIGIN);
        registry.add("capmoney.api-subnet", () -> "127.0.0.0/8");
        registry.add("capmoney.edge-secret-file", edge::toString);
        registry.add("capmoney.jwt-key-file", edge::toString);
        registry.add("capmoney.jwt-lookup-key-file", edge::toString);
    }
    HttpRequest.Builder request(String path, String ip) {
        return HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + path)).timeout(java.time.Duration.ofSeconds(8))
            .header("X-CM-Edge-Auth", EDGE).header("X-Forwarded-For", ip).header("X-Real-IP", ip);
    }
    @Test
    void setupRejectsMissingCsrfAndInvalidPasswordWithoutCreatingAccount() throws Exception {
        var documentation = client.send(request("/v3/api-docs", "192.0.2.1").GET().build(), HttpResponse.BodyHandlers.ofString());
        assertEquals(200, documentation.statusCode());
        var api = new tools.jackson.databind.ObjectMapper().readTree(documentation.body());
        var setupOperation = api.path("paths").path("/api/manager/setup").path("post");
        for (String status : java.util.List.of("204", "400", "403", "409", "413", "415", "429", "503")) {
            assertTrue(setupOperation.path("responses").has(status), "OpenAPI setup response " + status);
        }
        assertFalse(setupOperation.path("responses").has("200"));
        assertTrue(api.path("paths").path("/api/auth/csrf").path("get").path("responses").has("204"));
        assertTrue(api.path("components").path("schemas").path("SetupRequest").path("properties").path("password").path("writeOnly").asBoolean());
        String payload = "{\"token\":\"" + "a".repeat(43) + "\",\"password\":\"short\",\"passwordConfirmation\":\"short\"}";
        var rejected = client.send(request("/api/manager/setup", "192.0.2.1").header("Origin", ORIGIN)
            .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(payload)).build(), HttpResponse.BodyHandlers.ofString());
        assertEquals(403, rejected.statusCode());
        var csrf = client.send(request("/api/auth/csrf", "192.0.2.1").GET().build(), HttpResponse.BodyHandlers.ofString());
        assertEquals(204, csrf.statusCode());
        String cookie = csrf.headers().allValues("set-cookie").stream().filter(s -> s.startsWith("XSRF-TOKEN="))
            .findFirst().orElseThrow().split(";", 2)[0];
        var invalid = client.send(request("/api/manager/setup", "192.0.2.1").header("Origin", ORIGIN)
            .header("Content-Type", "application/json").header("Cookie", cookie)
            .header("X-XSRF-TOKEN", cookie.substring("XSRF-TOKEN=".length()))
            .POST(HttpRequest.BodyPublishers.ofString(payload)).build(), HttpResponse.BodyHandlers.ofString());
        assertEquals(400, invalid.statusCode());
        assertTrue(invalid.body().contains("PASSWORD_INVALID"));
        assertEquals(0L, jdbc.queryForObject("SELECT count(*) FROM manager_account", Long.class));
        for (String badPassword : java.util.List.of("a".repeat(73), "é".repeat(37))) {
            assertEquals(400, post("a".repeat(43), badPassword, badPassword, "192.0.2.2").statusCode());
        }
        assertEquals(400, post("a".repeat(43), "correct-password", "different-password", "192.0.2.2").statusCode());
        assertEquals(400, post("bad", "correct-password", "correct-password", "192.0.2.2").statusCode());
        assertEquals(400, post("a".repeat(43), "correct\0password", "correct\0password", "192.0.2.2").statusCode());
        assertEquals(400, rawPost("null", "192.0.2.2").statusCode());
        assertEquals(400, rawPost("{", "192.0.2.2").statusCode());
        String surrogate = "{\"token\":\"" + "a".repeat(43) + "\",\"password\":\"" + "\\ud800".repeat(12)
            + "\",\"passwordConfirmation\":\"" + "\\ud800".repeat(12) + "\"}";
        assertEquals(400, rawPost(surrogate, "192.0.2.2").statusCode());
        assertEquals(405, client.send(request("/api/manager/setup", "192.0.2.2").GET().build(), HttpResponse.BodyHandlers.ofString()).statusCode());
        String oversized = "{\"padding\":\"" + "x".repeat(4096) + "\"}";
        var large = client.send(request("/api/manager/setup", "192.0.2.3").header("Origin", ORIGIN)
            .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofInputStream(
                () -> new java.io.ByteArrayInputStream(oversized.getBytes(java.nio.charset.StandardCharsets.UTF_8)))).build(), HttpResponse.BodyHandlers.ofString());
        assertEquals(413, large.statusCode());
        assertEquals(401, client.send(request("/api/unknown", "192.0.2.3").GET().build(), HttpResponse.BodyHandlers.ofString()).statusCode());

        String token = java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(new byte[32]);
        String digest = java.util.HexFormat.of().formatHex(java.security.MessageDigest.getInstance("SHA-256")
            .digest(token.getBytes(java.nio.charset.StandardCharsets.US_ASCII)));
        fixture(digest, "manager@example.test", "72 hours");
        assertEquals(409, post(token, "correct-password", "correct-password", "192.0.2.4").statusCode());
        fixture(digest, "rollback@example.test", "71 hours 59 minutes");
        jdbc.execute("ALTER TABLE manager_account ADD CONSTRAINT test_force_rollback CHECK (email <> 'rollback@example.test')");
        assertEquals(503, post(token, "correct-password", "correct-password", "192.0.2.5").statusCode());
        assertEquals("issued", jdbc.queryForObject("SELECT state FROM manager_bootstrap", String.class));
        assertEquals(0L, jdbc.queryForObject("SELECT count(*) FROM manager_account", Long.class));
        fixture(digest, "manager@example.test", "71 hours 59 minutes");
        redis.getDockerClient().pauseContainerCmd(redis.getContainerId()).exec();
        try { assertEquals(503, post(token, "correct-password", "correct-password", "192.0.2.6").statusCode()); }
        finally { redis.getDockerClient().unpauseContainerCmd(redis.getContainerId()).exec(); }
        assertEquals("issued", jdbc.queryForObject("SELECT state FROM manager_bootstrap", String.class));
        for (int attempt = 0; attempt < 20; attempt++) {
            try { if ("PONG".equals(redisTemplate.getConnectionFactory().getConnection().ping())) break; }
            catch (RuntimeException unavailable) { Thread.sleep(100); }
        }
        for (int attempt = 0; attempt < 10; attempt++) {
            assertEquals(409, post("b".repeat(43), "correct-password", "correct-password", "192.0.2.7").statusCode());
        }
        var limited = post(token, "correct-password", "correct-password", "192.0.2.7");
        assertEquals(429, limited.statusCode());
        long retry = Long.parseLong(limited.headers().firstValue("retry-after").orElseThrow());
        assertTrue(retry > 0 && retry <= 900);
        Thread.sleep(1100);
        assertTrue(Long.parseLong(post(token, "correct-password", "correct-password", "192.0.2.7")
            .headers().firstValue("retry-after").orElseThrow()) < retry);
        long started = System.nanoTime();
        var first = java.util.concurrent.CompletableFuture.supplyAsync(() -> uncheckedPost(token, "192.0.2.8"));
        var second = java.util.concurrent.CompletableFuture.supplyAsync(() -> uncheckedPost(token, "192.0.2.9"));
        assertEquals(java.util.Set.of(204, 409), new java.util.HashSet<>(java.util.List.of(first.get(), second.get())));
        System.out.println("setup BCrypt12 concurrent request milliseconds=" + (System.nanoTime() - started) / 1_000_000);
        assertEquals(1L, jdbc.queryForObject("SELECT count(*) FROM manager_account", Long.class));
        assertEquals("consumed", jdbc.queryForObject("SELECT state FROM manager_bootstrap", String.class));
        assertNull(jdbc.queryForObject("SELECT token_digest FROM manager_bootstrap", String.class));
        String hash = jdbc.queryForObject("SELECT password_hash FROM manager_account", String.class);
        assertTrue(hash.startsWith("$2a$12$") || hash.startsWith("$2b$12$"));
        assertTrue(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder(12).matches("correct-password", hash));
        assertEquals(409, post(token, "correct-password", "correct-password", "192.0.2.10").statusCode());
        // A simultaneous HTTP burst must reject excess hash work instead of queueing it without bound.
        try (var workers = java.util.concurrent.Executors.newFixedThreadPool(12)) {
            var start = new java.util.concurrent.CountDownLatch(1);
            var burst = new java.util.ArrayList<java.util.concurrent.Future<Integer>>();
            for (int index = 0; index < 12; index++) {
                String ip = "192.0.2." + (100 + index);
                burst.add(workers.submit(() -> { start.await(); return uncheckedPost(token, ip); }));
            }
            start.countDown();
            var statuses = new java.util.ArrayList<Integer>();
            for (var result : burst) statuses.add(result.get());
            assertTrue(statuses.contains(429), "hash concurrency limit must reject a simultaneous burst");
            assertTrue(statuses.stream().allMatch(status -> status == 429 || status == 409));
        }
    }
    int uncheckedPost(String token, String ip) {
        try { return post(token, "correct-password", "correct-password", ip).statusCode(); }
        catch (Exception error) { throw new RuntimeException("HTTP test request failed"); }
    }
    void fixture(String digest, String email, String age) {
        jdbc.update("WITH stamp AS (SELECT clock_timestamp() AS t) UPDATE manager_bootstrap SET email=?, generation=generation+1, "
            + "state='issued', token_digest=?, accepted_at=stamp.t - CAST(? AS interval), "
            + "expires_at=stamp.t - CAST(? AS interval) + interval '72 hours', send_started_at=stamp.t - interval '72 hours' FROM stamp",
            email, digest, age, age);
    }
    HttpResponse<String> post(String token, String password, String confirmation, String ip) throws Exception {
        var json = new tools.jackson.databind.ObjectMapper().writeValueAsString(java.util.Map.of(
            "token", token, "password", password, "passwordConfirmation", confirmation));
        return rawPost(json, ip);
    }
    HttpResponse<String> rawPost(String json, String ip) throws Exception {
        var csrf = client.send(request("/api/auth/csrf", ip).GET().build(), HttpResponse.BodyHandlers.ofString());
        String cookie = csrf.headers().allValues("set-cookie").stream().filter(value -> value.startsWith("XSRF-TOKEN="))
            .findFirst().orElseThrow().split(";", 2)[0];
        return client.send(request("/api/manager/setup", ip).header("Origin", ORIGIN).header("Content-Type", "application/json")
            .header("Cookie", cookie).header("X-XSRF-TOKEN", cookie.substring("XSRF-TOKEN=".length()))
            .POST(HttpRequest.BodyPublishers.ofString(json)).build(), HttpResponse.BodyHandlers.ofString());
    }
}
