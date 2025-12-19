package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

@Entity
@Table(
    name = "vendor_tiers",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "tier_name")
    }
)
public class VendorTier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "tier_name", nullable = false, unique = true)
    private String tierName;
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    @Column(nullable = false)
    private Double minScoreThreshold;
    @Column(length = 500)
    private String description;
    @Column(nullable = false)
    private Boolean active;

    protected VendorTier() {}

    public VendorTier(String tierName, Double minScoreThreshold, String description) {
        this.tierName = tierName;
        this.minScoreThreshold = minScoreThreshold;
        this.description = description;
    }

    @PrePersist
    protected void onCreate() {
        if (this.active == null) {
            this.active = true;
        }
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    public Double getMinScoreThreshold() {
        return minScoreThreshold;
    }

    public void setMinScoreThreshold(Double minScoreThreshold) {
        this.minScoreThreshold = minScoreThreshold;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
     public VendorTier(Long id, String tierName, @DecimalMin("0.0") @DecimalMax("100.0") Double minScoreThreshold,
            String description, Boolean active) {
        this.id = id;
        this.tierName = tierName;
        this.minScoreThreshold = minScoreThreshold;
        this.description = description;
        this.active = active;
    }
}
