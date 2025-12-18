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
    public Vendor(Long id, String name, String contactEmail, String contactPhone, Boolean active, Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

}
