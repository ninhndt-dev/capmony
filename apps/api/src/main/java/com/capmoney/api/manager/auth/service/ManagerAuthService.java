package com.capmoney.api.manager.auth.service;

import com.capmoney.api.http.ApiProblem;
import com.capmoney.api.manager.auth.repository.ManagerAuthStore;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
public class ManagerAuthService {
    public record Pair(String access, String refresh, long refreshMaxAgeSeconds) {}
    public record LogoutResult(boolean allRevoked, String code) {}
    private static final String DUMMY_HASH = new BCryptPasswordEncoder(12).encode("capmoney-dummy-password");
    private final ManagerAuthStore store;
    private final ManagerTokens tokens;
    private final LoginThrottle throttle;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final Semaphore hashes = new Semaphore(4);
    public ManagerAuthService(ManagerAuthStore store, ManagerTokens tokens, LoginThrottle throttle) {
        this.store = store; this.tokens = tokens; this.throttle = throttle;
    }
    public Pair login(String email, String password, String trustedIp) {
        if (email == null || password == null || email.length() > 320 || password.length() > 1024) throw new ApiProblem(401, "LOGIN_FAILED");
        throttle.acquire(trustedIp);
        var account = store.account();
        if (!hashes.tryAcquire()) throw new ApiProblem(429, "RATE_LIMITED", 1);
        boolean valid;
        try { valid = encoder.matches(password, account == null ? DUMMY_HASH : account.passwordHash()); }
        finally { hashes.release(); }
        if (account == null || !valid || !account.email().equalsIgnoreCase(email.trim())) throw new ApiProblem(401, "LOGIN_FAILED");
        UUID familyId = UUID.randomUUID();
        String jti = tokens.newJti();
        var family = store.login(account.passwordHash(), familyId, tokens.digest(jti));
        // Logout may commit after this read; the next protected request will still reject that family.
        store.activeEmail(familyId, family.epoch());
        return pair(family, jti);
    }
    public Pair refresh(String refreshCookie) {
        var claims = tokens.verify(refreshCookie, "refresh");
        String nextJti = tokens.newJti();
        var result = store.refresh(claims.familyId(), claims.epoch(), tokens.digest(claims.jti()), tokens.digest(nextJti));
        if (result.reused()) throw new ApiProblem(401, "REFRESH_REUSED");
        return pair(result.family(), nextJti);
    }
    public String me(String accessCookie) {
        var claims = tokens.verify(accessCookie, "access");
        return store.activeEmail(claims.familyId(), claims.epoch());
    }
    public LogoutResult logout(String accessCookie, String refreshCookie) {
        ManagerTokens.Claims claims = null;
        boolean viaAccess = false;
        try { claims = tokens.verify(accessCookie, "access"); viaAccess = true; }
        catch (ApiProblem missingAccess) {
            try { claims = tokens.verify(refreshCookie, "refresh"); }
            catch (ApiProblem missingRefresh) { return new LogoutResult(false, "AUTH_REQUIRED"); }
        }
        var result = store.logout(claims.familyId(), claims.epoch(), viaAccess ? null : tokens.digest(claims.jti()), viaAccess);
        return switch (result) {
            case DONE -> new LogoutResult(true, "");
            case REUSED -> new LogoutResult(false, "REFRESH_REUSED");
            case DENIED -> new LogoutResult(false, "AUTH_REQUIRED");
        };
    }
    private Pair pair(ManagerAuthStore.Family family, String refreshJti) {
        String access = tokens.issue("access", family.id(), family.epoch(), tokens.newJti(), Instant.now().plusSeconds(15 * 60));
        String refresh = tokens.issue("refresh", family.id(), family.epoch(), refreshJti, family.expiresAt());
        long remaining = Math.max(0, java.time.Duration.between(Instant.now(), family.expiresAt()).toSeconds());
        return new Pair(access, refresh, remaining);
    }
}
