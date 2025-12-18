package com.example.demo.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
    name = "vendors",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
    }
)
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(name = "contact_email")
    private String contactEmail;
    @Column(name = "contact_phone")
    private String contactPhone;
    @Column(nullable = false)
    private Boolean active = true;
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    @Column(nullable = false)
    private Instant updatedAt;
    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.active == null) {
            this.active = true;
        }
    }
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

}
