package com.capmoney.api.health.service;

import com.capmoney.api.health.model.HealthStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.jdbc.core.JdbcTemplate;

class HealthServiceTest {
    @Test
    void databaseOutageAndRecoveryAreReflectedByTheSameService() {
        JdbcTemplate database = mock(JdbcTemplate.class);
        RedisConnectionFactory redis = mock(RedisConnectionFactory.class);
        RedisConnection connection = mock(RedisConnection.class);
        when(database.queryForObject("SELECT 1", Integer.class))
                .thenThrow(new DataAccessResourceFailureException("unavailable"))
                .thenThrow(new DataAccessResourceFailureException("unavailable"))
                .thenReturn(1);
        when(redis.getConnection()).thenReturn(connection);
        when(connection.ping()).thenReturn("PONG");
        HealthService service = new HealthService(database, redis);

        assertEquals(HealthStatus.DB_UNAVAILABLE, service.health());
        assertEquals(HealthStatus.DB_UNAVAILABLE, service.authReadiness());
        assertEquals(HealthStatus.UP, service.authReadiness());
    }

    @Test
    void redisOutageOnlyDegradesAuthReadinessAndCanRecover() {
        JdbcTemplate database = mock(JdbcTemplate.class);
        RedisConnectionFactory redis = mock(RedisConnectionFactory.class);
        RedisConnection connection = mock(RedisConnection.class);
        when(database.queryForObject("SELECT 1", Integer.class)).thenReturn(1);
        when(redis.getConnection()).thenThrow(new IllegalStateException("unavailable"))
                .thenReturn(connection);
        when(connection.ping()).thenReturn("PONG");
        HealthService service = new HealthService(database, redis);

        assertEquals(HealthStatus.UP, service.health());
        var degraded = service.authReadiness();
        assertEquals(HealthStatus.REDIS_UNAVAILABLE, degraded);
        assertEquals("redis_unavailable", degraded.code());
        assertEquals(HealthStatus.UP, service.authReadiness());
    }
}
