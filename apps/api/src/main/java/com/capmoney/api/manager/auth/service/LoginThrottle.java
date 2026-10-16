package com.capmoney.api.manager.auth.service;

import com.capmoney.api.http.ApiProblem;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

@Service
@org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
public class LoginThrottle {
    private static final DefaultRedisScript<List> SCRIPT = new DefaultRedisScript<>("""
        local ip = tonumber(redis.call('GET', KEYS[1]) or '0')
        local account = tonumber(redis.call('GET', KEYS[2]) or '0')
        if ip >= 10 or account >= 60 then
          local ttl = math.max(redis.call('PTTL', KEYS[1]), redis.call('PTTL', KEYS[2]))
          return {0, ttl}
        end
        local nextIp = redis.call('INCR', KEYS[1])
        if nextIp == 1 then redis.call('PEXPIRE', KEYS[1], 900000) end
        local nextAccount = redis.call('INCR', KEYS[2])
        if nextAccount == 1 then redis.call('PEXPIRE', KEYS[2], 900000) end
        return {1, 0}
        """, List.class);
    private final StringRedisTemplate redis;
    private final ManagerTokens tokens;
    public LoginThrottle(StringRedisTemplate redis, ManagerTokens tokens) { this.redis = redis; this.tokens = tokens; }
    public void acquire(String trustedIp) {
        if (trustedIp == null) throw new ApiProblem(403, "CSRF_ORIGIN");
        try {
            String hash = tokens.digest("ip:" + trustedIp);
            List<?> result = redis.execute(SCRIPT, List.of("capmoney:login:ip:" + hash, "capmoney:login:manager:1"));
            if (result == null || result.size() != 2) throw new IllegalStateException("redis_result");
            if (((Number) result.get(0)).longValue() != 1) {
                long ttl = ((Number) result.get(1)).longValue();
                throw new ApiProblem(429, "RATE_LIMITED", Math.max(1, (ttl + 999) / 1000));
            }
        } catch (ApiProblem expected) { throw expected; }
        catch (Exception unavailable) { throw new ApiProblem(503, "DEPENDENCY_UNAVAILABLE"); }
    }
}
