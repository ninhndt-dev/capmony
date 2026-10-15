package com.capmoney.api.manager.auth;

import static org.junit.jupiter.api.Assertions.*;
import java.net.*;
import java.net.http.*;
import java.nio.file.*;
import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {"spring.config.on-not-found=ignore", "capmoney.security.cookie-secure=false"})
@ActiveProfiles("test")
@EnabledIfEnvironmentVariable(named = "CAPMONEY_CONTAINER_TESTS", matches = "true")
class AuthHttpIntegrationTest {
    static final String EDGE = "c".repeat(64);
    static final String ORIGIN = "http://127.0.0.1:28080";
    static PostgreSQLContainer postgres;
    static GenericContainer<?> redis;
    @LocalServerPort int port;
    @Autowired JdbcTemplate jdbc;
    final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build();

    @DynamicPropertySource
    static void dependencies(DynamicPropertyRegistry registry) throws Exception {
        assertEquals("true", System.getenv("TESTCONTAINERS_RYUK_DISABLED"));
        assertEquals("true", System.getenv("TESTCONTAINERS_REUSE_ENABLE"));
        postgres = new PostgreSQLContainer("postgres:17.7-alpine3.22").withPassword(UUID.randomUUID().toString()).withReuse(true);
        redis = new GenericContainer<>("redis:7.4.6-alpine3.21").withExposedPorts(6379)
                .withEnv("CAPMONEY_AUTH_TEST", UUID.randomUUID().toString()).withReuse(true);
        postgres.start(); redis.start();
        Path edge = Files.createTempFile("capmoney-auth-edge-", ".test");
        Path jwt = Files.createTempFile("capmoney-auth-jwt-", ".test");
        Files.writeString(edge, EDGE); Files.writeString(jwt, "d".repeat(64));
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
        registry.add("capmoney.origin", () -> ORIGIN);
        registry.add("capmoney.api-subnet", () -> "127.0.0.0/8");
        registry.add("capmoney.edge-secret-file", edge::toString);
        registry.add("capmoney.jwt-key-file", jwt::toString);
        registry.add("capmoney.jwt-lookup-key-file", jwt::toString);
    }
    HttpRequest.Builder request(String path, String ip) {
        return HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + path)).timeout(Duration.ofSeconds(10))
                .header("X-CM-Edge-Auth", EDGE).header("X-Forwarded-For", ip).header("X-Real-IP", ip);
    }
    HttpResponse<String> get(String path, String ip, String cookies) throws Exception {
        var builder = request(path, ip).GET();
        if (cookies != null) builder.header("Cookie", cookies);
        return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }
    String csrf(String ip) throws Exception {
        var response = get("/api/auth/csrf", ip, null);
        assertEquals(204, response.statusCode());
        return cookie(response, "XSRF-TOKEN");
    }
    HttpResponse<String> post(String path, String ip, String csrf, String cookies, String json) throws Exception {
        var builder = request(path, ip).header("Origin", ORIGIN).header("Content-Type", "application/json")
                .header("X-XSRF-TOKEN", csrf.substring("XSRF-TOKEN=".length()))
                .header("Cookie", csrf + (cookies == null ? "" : "; " + cookies))
                .POST(HttpRequest.BodyPublishers.ofString(json));
        return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }
    String cookie(HttpResponse<?> response, String name) {
        return response.headers().allValues("set-cookie").stream().filter(value -> value.startsWith(name + "="))
                .findFirst().orElseThrow().split(";", 2)[0];
    }
    @Test
    void loginRefreshReuseAndLogoutAllDevices() throws Exception {
        String ip = "192.0.2.101";
        assertEquals(401, get("/api/manager/me", ip, null).statusCode());
        jdbc.update("INSERT INTO manager_account(id,email,password_hash,created_at) VALUES(1,?,?,clock_timestamp())",
                "manager@example.test", new BCryptPasswordEncoder(12).encode("correct-password"));
        String csrf = csrf(ip);
        var wrong = post("/api/auth/login", ip, csrf, null,
                "{\"email\":\"manager@example.test\",\"password\":\"wrong-password\"}");
        assertEquals(401, wrong.statusCode());
        assertEquals(0L, jdbc.queryForObject("SELECT count(*) FROM manager_session_family", Long.class));

        var login = post("/api/auth/login", ip, csrf, null,
                "{\"email\":\"manager@example.test\",\"password\":\"correct-password\"}");
        assertEquals(204, login.statusCode(), login.body());
        String access = cookie(login, "cm_dev_access");
        String refresh = cookie(login, "cm_dev_refresh");
        assertTrue(login.headers().allValues("set-cookie").stream().filter(s -> s.startsWith("cm_dev_")).allMatch(s -> s.contains("HttpOnly") && s.contains("SameSite=Lax")));
        assertEquals(200, get("/api/manager/me", ip, access).statusCode());
        assertEquals(403, client.send(request("/api/auth/refresh", ip).header("Origin", ORIGIN)
                .header("Cookie", refresh).POST(HttpRequest.BodyPublishers.noBody()).build(), HttpResponse.BodyHandlers.ofString()).statusCode());

        var rotated = post("/api/auth/refresh", ip, csrf(ip), refresh, "{}");
        assertEquals(204, rotated.statusCode(), rotated.body());
        String nextAccess = cookie(rotated, "cm_dev_access");
        assertEquals(200, get("/api/manager/me", ip, access).statusCode(), "old access stays valid after rotation");
        var replay = post("/api/auth/refresh", ip, csrf(ip), refresh, "{}");
        assertEquals(401, replay.statusCode(), replay.body());
        assertTrue(replay.body().contains("REFRESH_REUSED"));
        assertEquals(401, get("/api/manager/me", ip, nextAccess).statusCode());

        var first = post("/api/auth/login", "192.0.2.102", csrf("192.0.2.102"), null,
                "{\"email\":\"manager@example.test\",\"password\":\"correct-password\"}");
        var second = post("/api/auth/login", "192.0.2.103", csrf("192.0.2.103"), null,
                "{\"email\":\"manager@example.test\",\"password\":\"correct-password\"}");
        assertEquals(204, first.statusCode()); assertEquals(204, second.statusCode());
        String firstAccess = cookie(first, "cm_dev_access");
        String secondAccess = cookie(second, "cm_dev_access");
        assertEquals(200, get("/api/manager/me", "192.0.2.103", secondAccess).statusCode());
        var logout = post("/api/auth/logout", "192.0.2.102", csrf("192.0.2.102"), firstAccess, "{}");
        assertEquals(204, logout.statusCode(), logout.body());
        assertEquals(401, get("/api/manager/me", "192.0.2.103", secondAccess).statusCode());
        assertEquals(0L, jdbc.queryForObject("SELECT count(*) FROM manager_session_family WHERE revoked_at IS NULL", Long.class));

        var staleOwner = post("/api/auth/login", "192.0.2.104", csrf("192.0.2.104"), null,
                "{\"email\":\"manager@example.test\",\"password\":\"correct-password\"}");
        var otherPhone = post("/api/auth/login", "192.0.2.105", csrf("192.0.2.105"), null,
                "{\"email\":\"manager@example.test\",\"password\":\"correct-password\"}");
        assertEquals(204, staleOwner.statusCode()); assertEquals(204, otherPhone.statusCode());
        String staleAccess = cookie(staleOwner, "cm_dev_access");
        String staleRefresh = cookie(staleOwner, "cm_dev_refresh");
        assertEquals(204, post("/api/auth/refresh", "192.0.2.104", csrf("192.0.2.104"), staleRefresh, "{}").statusCode());
        assertEquals(204, post("/api/auth/logout", "192.0.2.104", csrf("192.0.2.104"),
                staleAccess + "; " + staleRefresh, "{}").statusCode(), "valid access must win over stale refresh");
        assertEquals(401, get("/api/manager/me", "192.0.2.105", cookie(otherPhone, "cm_dev_access")).statusCode());

        var staleOnly = post("/api/auth/login", "192.0.2.106", csrf("192.0.2.106"), null,
                "{\"email\":\"manager@example.test\",\"password\":\"correct-password\"}");
        var survivingPhone = post("/api/auth/login", "192.0.2.107", csrf("192.0.2.107"), null,
                "{\"email\":\"manager@example.test\",\"password\":\"correct-password\"}");
        assertEquals(204, staleOnly.statusCode()); assertEquals(204, survivingPhone.statusCode());
        String staleOnlyRefresh = cookie(staleOnly, "cm_dev_refresh");
        assertEquals(204, post("/api/auth/refresh", "192.0.2.106", csrf("192.0.2.106"), staleOnlyRefresh, "{}").statusCode());
        var denied = post("/api/auth/logout", "192.0.2.106", csrf("192.0.2.106"), staleOnlyRefresh, "{}");
        assertEquals(401, denied.statusCode());
        assertTrue(denied.body().contains("REFRESH_REUSED"));
        String survivingAccess = cookie(survivingPhone, "cm_dev_access");
        assertEquals(200, get("/api/manager/me", "192.0.2.107", survivingAccess).statusCode());

        redis.getDockerClient().pauseContainerCmd(redis.getContainerId()).exec();
        try {
            assertEquals(503, post("/api/auth/login", "192.0.2.108", csrf("192.0.2.108"), null,
                    "{\"email\":\"manager@example.test\",\"password\":\"correct-password\"}").statusCode());
            assertEquals(200, get("/api/manager/me", "192.0.2.107", survivingAccess).statusCode());
        } finally { redis.getDockerClient().unpauseContainerCmd(redis.getContainerId()).exec(); }

        postgres.getDockerClient().stopContainerCmd(postgres.getContainerId()).withTimeout(5).exec();
        try {
            assertEquals(503, get("/api/manager/me", "192.0.2.107", survivingAccess).statusCode());
            var uncertainLogout = post("/api/auth/logout", "192.0.2.107", csrf("192.0.2.107"), survivingAccess, "{}");
            assertEquals(503, uncertainLogout.statusCode());
            assertTrue(uncertainLogout.body().contains("REVOCATION_UNKNOWN"));
        } finally { postgres.getDockerClient().startContainerCmd(postgres.getContainerId()).exec(); }
    }
}
