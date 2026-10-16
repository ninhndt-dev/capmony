package com.capmoney.api.manager.bootstrap.repository;

import com.capmoney.api.manager.bootstrap.model.ManagerBootstrap;
import com.capmoney.api.http.ApiProblem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import java.time.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class BootstrapStore {
    // Longer than SMTP's three one-second operation timeouts; CAS still fences a late sender.
    private static final Duration SENDING_LEASE = Duration.ofSeconds(30);
    private final EntityManager entityManager;
    public BootstrapStore(EntityManager entityManager) { this.entityManager = entityManager; }
    public record Attempt(long generation, Instant startedAt) {}
    public enum LinkStatus { active, expired, retryable, unavailable }

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
    public String bootstrapEmail() {
        ManagerBootstrap row = lock();
        return managerExists() ? null : row.email;
    }

    @Transactional(timeout = 10)
    public Attempt reserve(String email, String digest) {
        ManagerBootstrap row = lock();
        Instant now = databaseNow();
        if (managerExists() || "consumed".equals(row.state)
                || (row.email != null && !row.email.equals(email))
                || row.renewalProofDigest != null
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

    private boolean matches(String expected, String digest) {
        return MessageDigest.isEqual((expected == null ? "0".repeat(64) : expected).getBytes(StandardCharsets.US_ASCII),
                digest.getBytes(StandardCharsets.US_ASCII));
    }

    @Transactional(timeout = 10)
    public LinkStatus linkStatus(String digest) {
        ManagerBootstrap row = lock();
        Instant now = databaseNow();
        if (managerExists() || "consumed".equals(row.state)) return LinkStatus.unavailable;
        if ("issued".equals(row.state) && matches(row.tokenDigest, digest)) {
            return row.expiresAt != null && row.expiresAt.isAfter(now) ? LinkStatus.active : LinkStatus.expired;
        }
        if (("failed".equals(row.state) || "unknown".equals(row.state)
                || ("sending".equals(row.state) && row.sendStartedAt != null
                    && !now.isBefore(row.sendStartedAt.plus(SENDING_LEASE))))
                && matches(row.renewalProofDigest, digest)) return LinkStatus.retryable;
        return LinkStatus.unavailable;
    }

    @Transactional(timeout = 10)
    public Attempt reserveRenewal(String email, String proofDigest, String newDigest) {
        ManagerBootstrap row = lock();
        Instant now = databaseNow();
        if (managerExists() || !email.equals(row.email) || "consumed".equals(row.state)) return null;
        boolean expired = "issued".equals(row.state) && row.expiresAt != null
                && !row.expiresAt.isAfter(now) && matches(row.tokenDigest, proofDigest);
        boolean retry = ("failed".equals(row.state) || "unknown".equals(row.state)
                || ("sending".equals(row.state) && row.sendStartedAt != null
                    && !now.isBefore(row.sendStartedAt.plus(SENDING_LEASE))))
                && matches(row.renewalProofDigest, proofDigest);
        if (!expired && !retry) return null;
        if (row.renewalReservedAt != null && now.isBefore(row.renewalReservedAt.plus(Duration.ofMinutes(15)))) {
            long seconds = Duration.between(now, row.renewalReservedAt.plus(Duration.ofMinutes(15))).toSeconds();
            throw new ApiProblem(429, "RATE_LIMITED", Math.max(1, seconds + 1));
        }
        if (expired) row.renewalProofDigest = proofDigest;
        row.renewalReservedAt = now;
        row.generation++;
        row.tokenDigest = newDigest;
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
        row.renewalProofDigest = null;
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
