package com.capmoney.api.manager.auth.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "manager_session_family")
public class ManagerSessionFamily {
    @Id public UUID id;
    @Column(name = "manager_id") public long managerId;
    @Column(name = "auth_epoch") public long authEpoch;
    @Column(name = "current_refresh_digest") public String currentRefreshDigest;
    @Column(name = "absolute_expires_at") public Instant absoluteExpiresAt;
    @Column(name = "revoked_at") public Instant revokedAt;
    @Column(name = "rotation_counter") public long rotationCounter;
    @Column(name = "created_at") public Instant createdAt;
    protected ManagerSessionFamily() {}
    public ManagerSessionFamily(UUID id, long managerId, long authEpoch, String digest, Instant createdAt) {
        this.id = id;
        this.managerId = managerId;
        this.authEpoch = authEpoch;
        this.currentRefreshDigest = digest;
        this.createdAt = createdAt;
        this.absoluteExpiresAt = createdAt.plusSeconds(7 * 24 * 60 * 60);
    }
}
