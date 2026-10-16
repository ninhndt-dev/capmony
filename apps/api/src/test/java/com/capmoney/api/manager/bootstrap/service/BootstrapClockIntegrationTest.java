package com.capmoney.api.manager.bootstrap.service;

import static org.junit.jupiter.api.Assertions.*;
import com.capmoney.api.manager.bootstrap.repository.BootstrapStore;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.*;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.postgresql.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = "spring.config.on-not-found=ignore")
@EnabledIfEnvironmentVariable(named = "CAPMONEY_CONTAINER_TESTS", matches = "true")
class BootstrapClockIntegrationTest {
    @Autowired BootstrapStore store;
    @Autowired JdbcTemplate jdbc;
    @DynamicPropertySource
    static void dependencies(DynamicPropertyRegistry registry) throws Exception {
        assertEquals("true", System.getenv("TESTCONTAINERS_RYUK_DISABLED"));
        assertEquals("true", System.getenv("TESTCONTAINERS_REUSE_ENABLE"));
        var postgres = new PostgreSQLContainer("postgres:17.7-alpine3.22")
                .withPassword(UUID.randomUUID().toString()).withReuse(true);
        postgres.start();
        var edge = Files.createTempFile("capmoney-bootstrap-clock-", ".test");
        Files.setPosixFilePermissions(edge, PosixFilePermissions.fromString("rw-------"));
        Files.writeString(edge, "b".repeat(64));
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("capmoney.origin", () -> "http://127.0.0.1:28080");
        registry.add("capmoney.api-subnet", () -> "172.30.49.0/24");
        registry.add("capmoney.edge-secret-file", edge::toString);
    }
    @Test
    void delayedCasKeepsAcceptedTimestampAndCannotResurrectFailedGeneration() throws Exception {
        var failed = store.reserve("manager@example.test", "a".repeat(64));
        store.fail(failed, "unknown");
        assertFalse(store.issue(failed, Instant.now(), Clock.systemUTC()));
        var current = store.reserve("manager@example.test", "b".repeat(64));
        // Align this positive-case acceptance fixture with the database clock. Colima's VM
        // was measured ~90ms ahead of the host, which correctly trips the production
        // acceptedAt >= sendStartedAt guard for an immediate synthetic host timestamp.
        Instant acceptedAt = jdbc.queryForObject("SELECT clock_timestamp()", java.sql.Timestamp.class).toInstant();
        Thread.sleep(250);
        assertFalse(store.issue(failed, acceptedAt, Clock.systemUTC()));
        assertTrue(store.issue(current, acceptedAt, Clock.systemUTC()));
        assertEquals(acceptedAt, jdbc.queryForObject("SELECT accepted_at FROM manager_bootstrap", java.sql.Timestamp.class).toInstant());
        assertEquals(acceptedAt.plus(Duration.ofHours(72)), jdbc.queryForObject("SELECT expires_at FROM manager_bootstrap", java.sql.Timestamp.class).toInstant());
        assertEquals("b".repeat(64), jdbc.queryForObject("SELECT token_digest FROM manager_bootstrap", String.class));
    }
}
