package com.capmoney.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.jdbc.core.JdbcTemplate;

class HealthControllerTest {
    @Test
    void databaseOutageAndRecoveryAreReflectedByTheSameController() {
        JdbcTemplate database = mock(JdbcTemplate.class);
        RedisConnectionFactory redis = mock(RedisConnectionFactory.class);
        RedisConnection connection = mock(RedisConnection.class);
        when(database.queryForObject("SELECT 1", Integer.class))
                .thenThrow(new DataAccessResourceFailureException("unavailable"))
                .thenThrow(new DataAccessResourceFailureException("unavailable"))
                .thenReturn(1);
        when(redis.getConnection()).thenReturn(connection);
        when(connection.ping()).thenReturn("PONG");
        HealthController controller = new HealthController(database, redis);

        assertEquals(503, controller.health().getStatusCode().value());
        assertEquals(503, controller.authReadiness().getStatusCode().value());
        assertEquals(200, controller.authReadiness().getStatusCode().value());
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
        HealthController controller = new HealthController(database, redis);

        assertEquals(200, controller.health().getStatusCode().value());
        var degraded = controller.authReadiness();
        assertEquals(503, degraded.getStatusCode().value());
        assertEquals("redis_unavailable", degraded.getBody().get("status"));
        assertEquals("no-store", degraded.getHeaders().getCacheControl());
        assertEquals(200, controller.authReadiness().getStatusCode().value());
    }
}
