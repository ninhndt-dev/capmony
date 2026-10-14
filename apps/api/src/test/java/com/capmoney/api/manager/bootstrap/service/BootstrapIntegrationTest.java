package com.capmoney.api.manager.bootstrap.service;

import static org.junit.jupiter.api.Assertions.*;
import com.capmoney.api.manager.bootstrap.repository.BootstrapStore;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.*;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;

/** Real database, retained Mailpit and two independent CLI JVMs. Never clears rows/containers. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = "spring.config.on-not-found=ignore")
@EnabledIfEnvironmentVariable(named = "CAPMONEY_CONTAINER_TESTS", matches = "true")
class BootstrapIntegrationTest {
    static PostgreSQLContainer postgres;
    static GenericContainer<?> smtp;
    static Path directory;
    static final String EMAIL = "manager@example.test";
    @Autowired BootstrapStore store;
    @Autowired JdbcTemplate jdbc;

    @DynamicPropertySource
    static void dependencies(DynamicPropertyRegistry registry) throws Exception {
        assertEquals("true", System.getenv("TESTCONTAINERS_RYUK_DISABLED"));
        assertEquals("true", System.getenv("TESTCONTAINERS_REUSE_ENABLE"));
        postgres = new PostgreSQLContainer("postgres:17.7-alpine3.22")
                .withPassword(UUID.randomUUID().toString()).withReuse(true);
        smtp = new GenericContainer<>("axllent/mailpit:v1.27.4")
                .withExposedPorts(1025, 8025).withReuse(true);
        postgres.start(); smtp.start();
        directory = Files.createTempDirectory("capmoney-bootstrap-test-");
        Files.setPosixFilePermissions(directory, PosixFilePermissions.fromString("rwx------"));
        Path edge = directory.resolve("edge-auth");
        Files.writeString(edge, "a".repeat(64));
        Files.setPosixFilePermissions(edge, PosixFilePermissions.fromString("rw-------"));
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("capmoney.origin", () -> "http://127.0.0.1:28080");
        registry.add("capmoney.api-subnet", () -> "172.30.49.0/24");
        registry.add("capmoney.edge-secret-file", edge::toString);
    }

    @Test
    void retryFencesOldGenerationAndTwoCliProcessesIssueOnlyOneLink() throws Exception {
        JavaMailSenderImpl refused = new JavaMailSenderImpl() {
            @Override public void send(SimpleMailMessage message) {
                throw new MailSendException("refused", new ConnectException());
            }
        };
        assertEquals("failed", issuer(refused, Clock.systemUTC()).issue().code());
        assertEquals("failed", state());
        // An actual SMTP server explicitly refuses DATA; this differs from a timeout after DATA.
        try (var server = new java.net.ServerSocket(0)) {
            Thread rejection = Thread.ofPlatform().daemon().start(() -> {
                try (var socket = server.accept();
                     var input = new java.io.BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
                     var output = new java.io.PrintWriter(socket.getOutputStream(), true)) {
                    output.print("220 localhost test\r\n"); output.flush();
                    for (String line; (line = input.readLine()) != null;) {
                        if (line.startsWith("DATA")) { output.print("550 message rejected\r\n"); }
                        else if (line.startsWith("QUIT")) { output.print("221 bye\r\n"); output.flush(); break; }
                        else { output.print("250 OK\r\n"); }
                        output.flush();
                    }
                } catch (java.io.IOException failure) { throw new java.io.UncheckedIOException(failure); }
            });
            JavaMailSenderImpl rejecting = new JavaMailSenderImpl();
            rejecting.setHost("127.0.0.1"); rejecting.setPort(server.getLocalPort());
            rejecting.getJavaMailProperties().setProperty("mail.smtp.timeout", "1000");
            assertEquals("failed", issuer(rejecting, Clock.systemUTC()).issue().code());
            rejection.join(2000);
        }
        JavaMailSenderImpl silent = new JavaMailSenderImpl() {
            @Override public void send(SimpleMailMessage message) {
                throw new MailSendException("timeout", new SocketTimeoutException());
            }
        };
        assertEquals("unknown", issuer(silent, Clock.systemUTC()).issue().code());
        assertEquals("unknown", state());
        JavaMailSenderImpl accepted = new JavaMailSenderImpl() {
            @Override public void send(SimpleMailMessage message) {}
        };
        assertEquals("unknown", issuer(accepted, Clock.offset(Clock.systemUTC(), Duration.ofMinutes(1))).issue().code());
        assertEquals("unknown", state());
        assertFalse(store.issue(new BootstrapStore.Attempt(1, Instant.now().minusSeconds(60)), Instant.now(), Clock.systemUTC()));

        Path properties = directory.resolve("cli.properties");
        Properties config = new Properties();
        config.setProperty("spring.config.import", "optional:configtree:/nonexistent-capmoney-test/");
        config.setProperty("spring.datasource.url", postgres.getJdbcUrl());
        config.setProperty("spring.datasource.username", postgres.getUsername());
        config.setProperty("spring.datasource.password", postgres.getPassword());
        config.setProperty("capmoney.origin", "http://127.0.0.1:28080");
        config.setProperty("capmoney.api-subnet", "172.30.49.0/24");
        config.setProperty("capmoney.edge-secret-file", directory.resolve("edge-auth").toString());
        config.setProperty("capmoney.bootstrap.enabled", "true");
        config.setProperty("capmoney.bootstrap.email", EMAIL);
        config.setProperty("spring.mail.host", smtp.getHost());
        config.setProperty("spring.mail.port", smtp.getMappedPort(1025).toString());
        try (var out = Files.newBufferedWriter(properties)) { config.store(out, "retained isolated CLI configuration"); }
        Files.setPosixFilePermissions(properties, PosixFilePermissions.fromString("rw-------"));
        Process first = startCli(properties, "first");
        Process second = startCli(properties, "second");
        assertTrue(first.waitFor(90, TimeUnit.SECONDS), "first CLI did not finish");
        assertTrue(second.waitFor(90, TimeUnit.SECONDS), "second CLI did not finish");
        assertEquals(Set.of(0, 2), new HashSet<>(List.of(first.exitValue(), second.exitValue())),
                "expected one accepted and one unavailable; inspect retained CLI logs");
        assertEquals("issued", state());
        assertEquals(5L, jdbc.queryForObject("SELECT generation FROM manager_bootstrap", Long.class));
        assertEquals(259200L, jdbc.queryForObject("SELECT extract(epoch FROM expires_at - accepted_at)::bigint FROM manager_bootstrap", Long.class));
        var response = java.net.http.HttpClient.newHttpClient().send(
                java.net.http.HttpRequest.newBuilder(java.net.URI.create("http://" + smtp.getHost() + ":"
                    + smtp.getMappedPort(8025) + "/api/v1/messages")).GET().build(),
                java.net.http.HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains(EMAIL));
        // A reusable inbox can contain older messages: match the received token digest to this generation.
        String expectedDigest = jdbc.queryForObject("SELECT token_digest FROM manager_bootstrap", String.class);
        var ids = java.util.regex.Pattern.compile("\"ID\"\\s*:\\s*\"([^\"]+)\"").matcher(response.body());
        boolean currentMessageReceived = false;
        while (ids.find()) {
            String messageBody = java.net.http.HttpClient.newHttpClient().send(
                    java.net.http.HttpRequest.newBuilder(java.net.URI.create("http://" + smtp.getHost() + ":"
                        + smtp.getMappedPort(8025) + "/api/v1/message/" + ids.group(1))).GET().build(),
                    java.net.http.HttpResponse.BodyHandlers.ofString()).body();
            var tokens = java.util.regex.Pattern.compile("#token=([A-Za-z0-9_-]{43})").matcher(messageBody);
            while (tokens.find()) {
                String digest = java.util.HexFormat.of().formatHex(java.security.MessageDigest.getInstance("SHA-256")
                        .digest(tokens.group(1).getBytes(java.nio.charset.StandardCharsets.US_ASCII)));
                currentMessageReceived |= expectedDigest.equals(digest);
            }
        }
        assertTrue(currentMessageReceived, "Mailpit must contain this generation, not just an earlier test email");
        assertEquals("unavailable", issuer(accepted, Clock.systemUTC()).issue().code());
        assertEquals("unavailable", new BootstrapIssuer(store, accepted, Clock.systemUTC(), true,
                "other@example.test", "http://127.0.0.1:28080").issue().code());
        assertFalse(Files.readString(directory.resolve("first.log")).contains("#token="));
        assertFalse(Files.readString(directory.resolve("second.log")).contains("#token="));
    }
    private BootstrapIssuer issuer(JavaMailSenderImpl sender, Clock clock) {
        return new BootstrapIssuer(store, sender, clock, true, EMAIL, "http://127.0.0.1:28080");
    }
    private String state() { return jdbc.queryForObject("SELECT state FROM manager_bootstrap", String.class); }
    private Process startCli(Path properties, String name) throws Exception {
        return new ProcessBuilder(Path.of(System.getProperty("java.home"), "bin/java").toString(),
                "-cp", System.getProperty("java.class.path"), "com.capmoney.api.ApiApplication",
                "--capmoney.bootstrap-manager", "--spring.config.on-not-found=ignore", "--spring.config.additional-location=" + properties.toUri())
                .redirectErrorStream(true).redirectOutput(directory.resolve(name + ".log").toFile()).start();
    }
}
