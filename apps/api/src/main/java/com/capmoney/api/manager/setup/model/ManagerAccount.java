package com.capmoney.api.manager.setup.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "manager_account")
public class ManagerAccount {
    @Id public Long id;
    public String email;
    @Column(name = "password_hash") public String passwordHash;
    @Column(name = "auth_epoch") public long authEpoch;
    @Column(name = "created_at") public Instant createdAt;
    protected ManagerAccount() {}
    public ManagerAccount(String email, String passwordHash, Instant createdAt) {
        this.id = 1L; this.email = email; this.passwordHash = passwordHash; this.createdAt = createdAt;
    }
}
