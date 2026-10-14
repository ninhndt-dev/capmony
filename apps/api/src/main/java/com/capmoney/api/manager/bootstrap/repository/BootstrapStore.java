package com.capmoney.api.manager.bootstrap.repository;

import com.capmoney.api.manager.bootstrap.model.ManagerBootstrap;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import java.time.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class BootstrapStore {
    // Longer than SMTP's three one-second operation timeouts; CAS still fences a late sender.
    private static final Duration SENDING_LEASE = Duration.ofSeconds(30);
    private final EntityManager entityManager;
    public BootstrapStore(EntityManager entityManager) { this.entityManager = entityManager; }
    public record Attempt(long generation, Instant startedAt) {}

    private ManagerBootstrap lock() {
        ManagerBootstrap row = entityManager.find(ManagerBootstrap.class, 1L, LockModeType.PESSIMISTIC_WRITE);
        if (row == null) throw new IllegalStateException("bootstrap_row_missing");
        return row;
    }
    private Instant databaseNow() {
        return ((java.sql.Timestamp) entityManager.createNativeQuery("SELECT clock_timestamp()", java.sql.Timestamp.class)
                .getSingleResult()).toInstant();
    }
    private boolean managerExists() {
        Object relation = entityManager.createNativeQuery("SELECT CAST(to_regclass('public.manager_account') AS text)").getSingleResult();
        return relation != null && Boolean.TRUE.equals(entityManager.createNativeQuery(
                "SELECT EXISTS (SELECT 1 FROM manager_account)", Boolean.class).getSingleResult());
    }

    @Transactional(timeout = 10)
    public Attempt reserve(String email, String digest) {
        ManagerBootstrap row = lock();
        Instant now = databaseNow();
        if (managerExists() || "consumed".equals(row.state)
                || (row.email != null && !row.email.equals(email))
                || "issued".equals(row.state)
                || ("sending".equals(row.state) && now.isBefore(row.sendStartedAt.plus(SENDING_LEASE)))) return null;
        row.email = email;
        row.generation++;
        row.tokenDigest = digest;
        row.state = "sending";
        row.sendStartedAt = now;
        row.acceptedAt = null;
        row.expiresAt = null;
        row.updatedAt = now;
        return new Attempt(row.generation, now);
    }

    @Transactional(timeout = 10)
    public boolean issue(Attempt attempt, Instant acceptedAt, Clock clock) {
        ManagerBootstrap row = lock();
        if (row.generation != attempt.generation() || !"sending".equals(row.state)) return false;
        Instant before = clock.instant();
        long started = System.nanoTime();
        Instant now = databaseNow();
        long elapsed = System.nanoTime() - started;
        Instant after = clock.instant();
        boolean clockValid = elapsed <= Duration.ofSeconds(5).toNanos()
                && !after.isBefore(before) && Duration.between(before, after).compareTo(Duration.ofSeconds(5)) <= 0
                && Duration.between(before, now).abs().compareTo(Duration.ofSeconds(5)) <= 0
                && Duration.between(after, now).abs().compareTo(Duration.ofSeconds(5)) <= 0;
        if (!clockValid || acceptedAt.isBefore(attempt.startedAt()) || acceptedAt.isAfter(now.plusSeconds(5))
                || !now.isBefore(acceptedAt.plus(Duration.ofHours(72))) || managerExists()) {
            row.state = "unknown";
            row.tokenDigest = null;
            row.updatedAt = now;
            return false;
        }
        row.state = "issued";
        row.acceptedAt = acceptedAt;
        row.expiresAt = acceptedAt.plus(Duration.ofHours(72));
        row.updatedAt = now;
        return true;
    }

    @Transactional(timeout = 10)
    public void fail(Attempt attempt, String state) {
        ManagerBootstrap row = lock();
        if (row.generation == attempt.generation() && "sending".equals(row.state)) {
            row.state = state;
            row.tokenDigest = null;
            row.updatedAt = databaseNow();
        }
    }
}
