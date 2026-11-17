package com.enterpriseflow.organisation;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "organisations")
public class Organisation {
    @Id
    private UUID id;
    @Column(nullable = false, unique = true, length = 120)
    private String name;
    @Column(nullable = false)
    private boolean active = true;
    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    protected Organisation() {}
    public Organisation(UUID id, String name) { this.id = id; this.name = name; }
    public UUID getId() { return id; }
    public String getName() { return name; }
    public boolean isActive() { return active; }
}
