package com.capmoney.api.manager.bootstrap.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "manager_bootstrap")
public class ManagerBootstrap {
    @Id public Long id;
    public String email;
    public long generation;
    @Column(name = "token_digest") public String tokenDigest;
    @Column(name = "renewal_proof_digest") public String renewalProofDigest;
    @Column(name = "renewal_reserved_at") public Instant renewalReservedAt;
    public String state;
    @Column(name = "send_started_at") public Instant sendStartedAt;
    @Column(name = "accepted_at") public Instant acceptedAt;
    @Column(name = "expires_at") public Instant expiresAt;
    @Column(name = "consumed_at") public Instant consumedAt;
    @Column(name = "updated_at") public Instant updatedAt;
    protected ManagerBootstrap() {}
}
