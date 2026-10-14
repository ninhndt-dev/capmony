package com.capmoney.api.manager.setup.repository;

import com.capmoney.api.http.ApiProblem;
import com.capmoney.api.manager.bootstrap.model.ManagerBootstrap;
import com.capmoney.api.manager.setup.model.ManagerAccount;
import jakarta.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ManagerSetupStore {
    private final EntityManager entityManager;
    public ManagerSetupStore(EntityManager entityManager) { this.entityManager = entityManager; }
    @Transactional(timeout = 10)
    public void complete(String digest, String passwordHash) {
        var bootstrap = entityManager.find(ManagerBootstrap.class, 1L, LockModeType.PESSIMISTIC_WRITE);
        Instant now = ((java.sql.Timestamp) entityManager.createNativeQuery("SELECT clock_timestamp()", java.sql.Timestamp.class).getSingleResult()).toInstant();
        String expected = bootstrap == null || bootstrap.tokenDigest == null ? "0".repeat(64) : bootstrap.tokenDigest;
        boolean tokenMatches = MessageDigest.isEqual(expected.getBytes(StandardCharsets.US_ASCII), digest.getBytes(StandardCharsets.US_ASCII));
        if (bootstrap == null || !tokenMatches || !"issued".equals(bootstrap.state)
            || bootstrap.expiresAt == null || !bootstrap.expiresAt.isAfter(now)
            || entityManager.find(ManagerAccount.class, 1L) != null) throw new ApiProblem(409, "SETUP_LINK_UNAVAILABLE");
        entityManager.persist(new ManagerAccount(bootstrap.email, passwordHash, now));
        bootstrap.state = "consumed";
        bootstrap.tokenDigest = null;
        bootstrap.consumedAt = now;
        bootstrap.updatedAt = now;
        entityManager.flush();
    }
}
