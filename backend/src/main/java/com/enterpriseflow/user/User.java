package com.enterpriseflow.user;

import com.enterpriseflow.organisation.Organisation;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = "uk_user_org_email", columnNames = {"organisation_id", "email"}))
public class User {
    @Id
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organisation_id", nullable = false)
    private Organisation organisation;
    @Column(nullable = false, length = 160)
    private String email;
    @Column(nullable = false, length = 255)
    private String passwordHash;
    @Column(nullable = false, length = 80)
    private String displayName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;
    @Column(nullable = false) private boolean enabled = true;
    @Column(nullable = false) private int failedLoginAttempts;
    private Instant lockedUntil;
    @Column(nullable = false) private Instant createdAt = Instant.now();

    protected User() {}
    public User(UUID id, Organisation organisation, String email, String passwordHash, String displayName, Role role) {
        this.id = id; this.organisation = organisation; this.email = email.toLowerCase();
        this.passwordHash = passwordHash; this.displayName = displayName; this.role = role;
    }
    public UUID getId() { return id; }
    public Organisation getOrganisation() { return organisation; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getDisplayName() { return displayName; }
    public Role getRole() { return role; }
    public boolean isEnabled() { return enabled && (lockedUntil == null || lockedUntil.isBefore(Instant.now())); }
    public void recordFailedLogin() { failedLoginAttempts++; if (failedLoginAttempts >= 5) lockedUntil = Instant.now().plusSeconds(900); }
    public void recordSuccessfulLogin() { failedLoginAttempts = 0; lockedUntil = null; }
}
