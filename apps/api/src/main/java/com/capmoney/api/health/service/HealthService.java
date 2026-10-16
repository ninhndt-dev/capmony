package com.capmoney.api.health.service;

import com.capmoney.api.health.model.HealthStatus;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class HealthService {
    private final JdbcTemplate database;
    private final RedisConnectionFactory redis;

    public HealthService(JdbcTemplate database, RedisConnectionFactory redis) {
        this.database = database;
        this.redis = redis;
    }

    public HealthStatus health() {
        return databaseIsUp() ? HealthStatus.UP : HealthStatus.DB_UNAVAILABLE;
    }

    public HealthStatus authReadiness() {
        if (!databaseIsUp()) return HealthStatus.DB_UNAVAILABLE;
        try (RedisConnection connection = redis.getConnection()) {
            if (!"PONG".equalsIgnoreCase(connection.ping())) return HealthStatus.REDIS_UNAVAILABLE;
        } catch (RuntimeException exception) {
            return HealthStatus.REDIS_UNAVAILABLE;
        }
        return HealthStatus.UP;
    }

    private boolean databaseIsUp() {
        try {
            return Integer.valueOf(1).equals(database.queryForObject("SELECT 1", Integer.class));
        } catch (RuntimeException exception) {
            return false;
        }
    }
}
