package com.capmoney.api.manager.bootstrap.service;

import com.capmoney.api.http.ApiProblem;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.List;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

@Service
public class ManagerLinkThrottle {
    private static final DefaultRedisScript<List> SCRIPT = new DefaultRedisScript<>("""
        local ip = tonumber(redis.call('GET', KEYS[1]) or '0')
        local global = tonumber(redis.call('GET', KEYS[2]) or '0')
        if ip >= tonumber(ARGV[1]) then return {0, redis.call('PTTL', KEYS[1])} end
        if global >= tonumber(ARGV[2]) then return {0, redis.call('PTTL', KEYS[2])} end
        if redis.call('INCR', KEYS[1]) == 1 then redis.call('PEXPIRE', KEYS[1], tonumber(ARGV[3])) end
        if redis.call('INCR', KEYS[2]) == 1 then redis.call('PEXPIRE', KEYS[2], tonumber(ARGV[4])) end
        return {1, 0}
        """, List.class);
    private final StringRedisTemplate redis;
    public ManagerLinkThrottle(StringRedisTemplate redis) { this.redis = redis; }

    public void acquire(String trustedIp, boolean renewal) {
        if (trustedIp == null) throw new ApiProblem(403, "CSRF_ORIGIN");
        try {
            String hash = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(trustedIp.getBytes(StandardCharsets.US_ASCII)));
            String prefix = renewal ? "capmoney:renew:" : "capmoney:link-status:";
            List<?> result = redis.execute(SCRIPT, List.of(prefix + hash, prefix + "global"),
                    renewal ? "3" : "30", renewal ? "10" : "300", "3600000", renewal ? "86400000" : "900000");
            if (result == null || result.size() != 2) throw new IllegalStateException("redis_result");
            if (((Number) result.get(0)).longValue() != 1) {
                long ttl = ((Number) result.get(1)).longValue();
                throw new ApiProblem(429, "RATE_LIMITED", Math.max(1, (ttl + 999) / 1000));
            }
        } catch (ApiProblem expected) { throw expected; }
        catch (Exception unavailable) { throw new ApiProblem(503, "DEPENDENCY_UNAVAILABLE"); }
    }
}
