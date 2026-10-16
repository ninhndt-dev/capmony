package com.capmoney.api.manager.bootstrap.service;

import com.capmoney.api.http.ApiProblem;
import com.capmoney.api.manager.bootstrap.repository.BootstrapStore;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import org.springframework.stereotype.Service;

@Service
public class ManagerLinkService {
    private final BootstrapStore store;
    private final BootstrapIssuer issuer;
    private final ManagerLinkThrottle throttle;
    public ManagerLinkService(BootstrapStore store, BootstrapIssuer issuer, ManagerLinkThrottle throttle) {
        this.store = store; this.issuer = issuer; this.throttle = throttle;
    }
    public BootstrapStore.LinkStatus status(String token, String ip) {
        String digest = digest(token);
        throttle.acquire(ip, false);
        return store.linkStatus(digest);
    }
    public void renew(String token, String ip) {
        String digest = digest(token);
        // Bound public proof checks before touching PostgreSQL. Only valid proof spends the send quota.
        throttle.acquire(ip, false);
        var state = store.linkStatus(digest);
        if (state != BootstrapStore.LinkStatus.expired && state != BootstrapStore.LinkStatus.retryable)
            throw new ApiProblem(409, "SETUP_LINK_UNAVAILABLE");
        throttle.acquire(ip, true);
        var result = issuer.renew(digest);
        switch (result.code()) {
            case "accepted" -> { return; }
            case "unavailable" -> throw new ApiProblem(409, "SETUP_LINK_UNAVAILABLE");
            default -> throw new ApiProblem(503, "DELIVERY_UNCONFIRMED");
        }
    }
    private String digest(String token) {
        if (token == null || !token.matches("[A-Za-z0-9_-]{43}")) throw new ApiProblem(400, "LINK_INVALID");
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.US_ASCII)));
        } catch (java.security.NoSuchAlgorithmException impossible) { throw new IllegalStateException(impossible); }
    }
}
