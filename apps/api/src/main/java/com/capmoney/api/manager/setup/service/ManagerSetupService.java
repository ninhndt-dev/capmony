package com.capmoney.api.manager.setup.service;

import com.capmoney.api.http.ApiProblem;
import com.capmoney.api.manager.setup.repository.ManagerSetupStore;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.concurrent.Semaphore;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ManagerSetupService {
    private final ManagerSetupStore store;
    private final SetupThrottle throttle;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final Semaphore hashes = new Semaphore(4);
    public ManagerSetupService(ManagerSetupStore store, SetupThrottle throttle) { this.store = store; this.throttle = throttle; }
    public void complete(String token, String password, String confirmation, String trustedIp) {
        if (token == null || !token.matches("[A-Za-z0-9_-]{43}") || password == null || !password.equals(confirmation)
            || password.indexOf('\0') >= 0 || !StandardCharsets.UTF_8.newEncoder().canEncode(password)) throw new ApiProblem(400, "PASSWORD_INVALID");
        int bytes = password.getBytes(StandardCharsets.UTF_8).length;
        if (bytes < 12 || bytes > 72) throw new ApiProblem(400, "PASSWORD_INVALID");
        throttle.acquire(trustedIp);
        if (!hashes.tryAcquire()) throw new ApiProblem(429, "RATE_LIMITED", 1);
        String passwordHash;
        try { passwordHash = encoder.encode(password); } finally { hashes.release(); }
        try {
            String digest = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.US_ASCII)));
            store.complete(digest, passwordHash);
        } catch (java.security.NoSuchAlgorithmException impossible) { throw new IllegalStateException(impossible); }
    }
}
