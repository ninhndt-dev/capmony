package com.capmoney.api.manager.setup.service;

import com.capmoney.api.http.ApiProblem;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

@Service
public class SetupThrottle {
    private static final DefaultRedisScript<List> SCRIPT = new DefaultRedisScript<>("""
        local count = tonumber(redis.call('GET', KEYS[1]) or '0')
        if count >= 10 then return {0, redis.call('PTTL', KEYS[1])} end
        local next = redis.call('INCR', KEYS[1])
        if next == 1 then redis.call('PEXPIRE', KEYS[1], 900000) end
        return {1, redis.call('PTTL', KEYS[1])}
        """, List.class);
    private final StringRedisTemplate redis;
    public SetupThrottle(StringRedisTemplate redis) { this.redis = redis; }
    public void acquire(String trustedIp) {
        if (trustedIp == null) throw new ApiProblem(403, "CSRF_ORIGIN");
        try {
            String hash = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(trustedIp.getBytes(StandardCharsets.US_ASCII)));
            List<?> result = redis.execute(SCRIPT, List.of("capmoney:setup:" + hash));
            if (result == null || result.size() != 2) throw new IllegalStateException("redis_result");
            if (((Number) result.get(0)).longValue() != 1) {
                long ttl = ((Number) result.get(1)).longValue();
                throw new ApiProblem(429, "RATE_LIMITED", Math.max(1, (ttl + 999) / 1000));
            }
        } catch (ApiProblem expected) { throw expected; }
        catch (Exception unavailable) { throw new ApiProblem(503, "DEPENDENCY_UNAVAILABLE"); }
    }
}
