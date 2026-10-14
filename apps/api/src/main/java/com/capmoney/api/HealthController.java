package com.capmoney.api;

import java.util.Map;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    private final JdbcTemplate database;
    private final RedisConnectionFactory redis;

    HealthController(JdbcTemplate database, RedisConnectionFactory redis) {
        this.database = database;
        this.redis = redis;
    }

    @GetMapping("/api/health")
    public ResponseEntity<Map<String, String>> health() {
        if (!databaseIsUp()) return result(HttpStatus.SERVICE_UNAVAILABLE, "db_unavailable");
        return result(HttpStatus.OK, "UP");
    }

    @GetMapping("/api/ready/auth")
    public ResponseEntity<Map<String, String>> authReadiness() {
        if (!databaseIsUp()) return result(HttpStatus.SERVICE_UNAVAILABLE, "db_unavailable");
        try (RedisConnection connection = redis.getConnection()) {
            if (!"PONG".equalsIgnoreCase(connection.ping())) return result(HttpStatus.SERVICE_UNAVAILABLE, "redis_unavailable");
        } catch (RuntimeException exception) {
            return result(HttpStatus.SERVICE_UNAVAILABLE, "redis_unavailable");
        }
        return result(HttpStatus.OK, "UP");
    }

    private boolean databaseIsUp() {
        try {
            return Integer.valueOf(1).equals(database.queryForObject("SELECT 1", Integer.class));
        } catch (RuntimeException exception) {
            return false;
        }
    }

    private ResponseEntity<Map<String, String>> result(HttpStatus status, String code) {
        return ResponseEntity.status(status).cacheControl(CacheControl.noStore()).body(Map.of("status", code));
    }
}
