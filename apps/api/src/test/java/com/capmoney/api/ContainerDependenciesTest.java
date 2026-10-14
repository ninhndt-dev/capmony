package com.capmoney.api;

import com.capmoney.api.health.controller.HealthController;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/** Opt-in: reusable containers and secret test files remain for inspection. */
@SpringBootTest(properties = "spring.config.on-not-found=ignore")
@EnabledIfEnvironmentVariable(named = "CAPMONEY_CONTAINER_TESTS", matches = "true")
class ContainerDependenciesTest {
    @Autowired
    private HealthController controller;

    @DynamicPropertySource
    static void dependencies(DynamicPropertyRegistry registry) throws IOException {
        assertEquals("true", System.getenv("TESTCONTAINERS_RYUK_DISABLED"));
        assertEquals("true", System.getenv("TESTCONTAINERS_REUSE_ENABLE"));
        PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17.7-alpine3.22")
                .withPassword(UUID.randomUUID().toString()).withReuse(true);
        GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7.4.6-alpine3.21"))
                .withExposedPorts(6379).withReuse(true);
        postgres.start();
        redis.start();
        Path secret = Files.createTempFile("capmoney-edge-", ".test");
        Files.setPosixFilePermissions(secret, PosixFilePermissions.fromString("rw-------"));
        Files.writeString(secret, UUID.randomUUID().toString().replace("-", "")
                + UUID.randomUUID().toString().replace("-", ""));
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
        registry.add("capmoney.origin", () -> "http://127.0.0.1:28080");
        registry.add("capmoney.api-subnet", () -> "172.30.49.0/24");
        registry.add("capmoney.edge-secret-file", secret::toString);
    }

    @Test
    void springContextHealthUsesRealPostgresAndRedis() {
        assertEquals(200, controller.health().getStatusCode().value());
        assertEquals(200, controller.authReadiness().getStatusCode().value());
    }
}
