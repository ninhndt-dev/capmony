package com.capmoney.api.manager.auth.repository;

import com.capmoney.api.http.ApiProblem;
import com.capmoney.api.manager.auth.model.ManagerSessionFamily;
import com.capmoney.api.manager.setup.model.ManagerAccount;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ManagerAuthStore {
    public record Account(String email, String passwordHash) {}
    public record Family(UUID id, long epoch, Instant expiresAt) {}
    public record Rotation(boolean reused, Family family) {}
    public enum Logout { DONE, REUSED, DENIED }

    private final EntityManager em;
    public ManagerAuthStore(EntityManager em) { this.em = em; }
    public Account account() {
        var manager = em.find(ManagerAccount.class, 1L);
        return manager == null ? null : new Account(manager.email, manager.passwordHash);
    }
    private Instant now() {
        return ((java.sql.Timestamp) em.createNativeQuery("SELECT clock_timestamp()", java.sql.Timestamp.class).getSingleResult()).toInstant();
    }
    @Transactional(timeout = 10)
    public Family login(String expectedHash, UUID familyId, String refreshDigest) {
        var manager = em.find(ManagerAccount.class, 1L, LockModeType.PESSIMISTIC_WRITE);
        if (manager == null || !manager.passwordHash.equals(expectedHash)) throw new ApiProblem(401, "LOGIN_FAILED");
        var family = new ManagerSessionFamily(familyId, 1L, manager.authEpoch, refreshDigest, now());
        em.persist(family);
        em.flush();
        return new Family(family.id, family.authEpoch, family.absoluteExpiresAt);
    }
    public String activeEmail(UUID familyId, long epoch) {
        var family = em.find(ManagerSessionFamily.class, familyId);
        if (family == null || family.managerId != 1L || family.authEpoch != epoch || family.revokedAt != null) throw new ApiProblem(401, "AUTH_REQUIRED");
        var manager = em.find(ManagerAccount.class, 1L);
        if (manager == null || manager.authEpoch != epoch || !family.absoluteExpiresAt.isAfter(now())) throw new ApiProblem(401, "AUTH_REQUIRED");
        return manager.email;
    }
    @Transactional(timeout = 10)
    public Rotation refresh(UUID familyId, long epoch, String oldDigest, String newDigest) {
        var manager = em.find(ManagerAccount.class, 1L, LockModeType.PESSIMISTIC_WRITE);
        if (manager == null || manager.authEpoch != epoch) throw new ApiProblem(401, "AUTH_REQUIRED");
        var family = em.find(ManagerSessionFamily.class, familyId, LockModeType.PESSIMISTIC_WRITE);
        Instant now = now();
        if (family == null || family.managerId != 1L || family.authEpoch != epoch || family.revokedAt != null
                || !family.absoluteExpiresAt.isAfter(now)) throw new ApiProblem(401, "AUTH_REQUIRED");
        if (!family.currentRefreshDigest.equals(oldDigest)) {
            family.revokedAt = now;
            em.flush();
            return new Rotation(true, null);
        }
        family.currentRefreshDigest = newDigest;
        family.rotationCounter++;
        em.flush();
        return new Rotation(false, new Family(family.id, family.authEpoch, family.absoluteExpiresAt));
    }
    @Transactional(timeout = 10)
    public Logout logout(UUID familyId, long epoch, String refreshDigest, boolean viaAccess) {
        var manager = em.find(ManagerAccount.class, 1L, LockModeType.PESSIMISTIC_WRITE);
        if (manager == null || manager.authEpoch != epoch) return Logout.DENIED;
        var family = em.find(ManagerSessionFamily.class, familyId, LockModeType.PESSIMISTIC_WRITE);
        Instant now = now();
        if (family == null || family.managerId != 1L || family.authEpoch != epoch || family.revokedAt != null
                || !family.absoluteExpiresAt.isAfter(now)) return Logout.DENIED;
        if (!viaAccess && !family.currentRefreshDigest.equals(refreshDigest)) {
            family.revokedAt = now;
            em.flush();
            return Logout.REUSED;
        }
        manager.authEpoch++;
        em.createQuery("UPDATE ManagerSessionFamily f SET f.revokedAt = :now WHERE f.managerId = 1 AND f.revokedAt IS NULL")
                .setParameter("now", now).executeUpdate();
        em.flush();
        return Logout.DONE;
    }
    @Transactional(timeout = 10)
    public void revokeAllForManager(long managerId) {
        if (managerId != 1L) throw new IllegalArgumentException("Unknown manager");
        var manager = em.find(ManagerAccount.class, managerId, LockModeType.PESSIMISTIC_WRITE);
        if (manager == null) throw new ApiProblem(401, "AUTH_REQUIRED");
        manager.authEpoch++;
        em.createQuery("UPDATE ManagerSessionFamily f SET f.revokedAt = :now WHERE f.managerId = :managerId AND f.revokedAt IS NULL")
                .setParameter("now", now()).setParameter("managerId", managerId).executeUpdate();
        em.flush();
    }
}
