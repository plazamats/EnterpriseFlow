package com.enterpriseflow.authentication;

import com.enterpriseflow.user.User;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens", indexes = @Index(name = "idx_refresh_token_hash", columnList = "token_hash"))
public class RefreshToken {
    @Id private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "token_hash", nullable = false, unique = true, length = 64) private String tokenHash;
    @Column(nullable = false) private Instant expiresAt;
    private Instant revokedAt;
    protected RefreshToken() {}
    public RefreshToken(UUID id, User user, String tokenHash, Instant expiresAt) {
        this.id = id; this.user = user; this.tokenHash = tokenHash; this.expiresAt = expiresAt;
    }
    public User getUser() { return user; }
    public boolean usable() { return revokedAt == null && expiresAt.isAfter(Instant.now()); }
    public void revoke() { revokedAt = Instant.now(); }
}
