package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import java.time.Instant;

@Entity
@Table(
    name = "vendor_performance_scores",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "vendor_id")
    }
)
public class VendorPerformanceScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One performance score per vendor
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    // 0–100
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    @Column(nullable = false)
    private Double onTimePercentage;

    // 0–100
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    @Column(nullable = false)
    private Double qualityCompliancePercentage;

    // 0–100 (derived)
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    @Column(nullable = false)
    private Double overallScore;

    @Column(nullable = false, updatable = false)
    private Instant calculatedAt;

    // Predefined weights (example)
    private static final double ON_TIME_WEIGHT = 0.6;
    private static final double QUALITY_WEIGHT = 0.4;

    protected VendorPerformanceScore() {}

    public VendorPerformanceScore(
            Vendor vendor,
            Double onTimePercentage,
            Double qualityCompliancePercentage
    ) {
        this.vendor = vendor;
        this.onTimePercentage = onTimePercentage;
        this.qualityCompliancePercentage = qualityCompliancePercentage;
    }

    @PrePersist
    @PreUpdate
    protected void calculateOverallScore() {
        this.overallScore =(onTimePercentage * ON_TIME_WEIGHT) +(qualityCompliancePercentage * QUALITY_WEIGHT);

        this.calculatedAt = Instant.now();
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public Double getOnTimePercentage() {
        return onTimePercentage;
    }

    public void setOnTimePercentage(Double onTimePercentage) {
        this.onTimePercentage = onTimePercentage;
    }

    public Double getQualityCompliancePercentage() {
        return qualityCompliancePercentage;
    }

    public void setQualityCompliancePercentage(Double qualityCompliancePercentage) {
        this.qualityCompliancePercentage = qualityCompliancePercentage;
    }

    public Double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(Double overallScore) {
        this.overallScore = overallScore;
    }

    public Instant getCalculatedAt() {
        return calculatedAt;
    }

    public void setCalculatedAt(Instant calculatedAt) {
        this.calculatedAt = calculatedAt;
    }

    public static double getOnTimeWeight() {
        return ON_TIME_WEIGHT;
    }

    public static double getQualityWeight() {
        return QUALITY_WEIGHT;
    }
    public VendorPerformanceScore(Long id, Vendor vendor,
            @DecimalMin("0.0") @DecimalMax("100.0") Double onTimePercentage,
            @DecimalMin("0.0") @DecimalMax("100.0") Double qualityCompliancePercentage,
            @DecimalMin("0.0") @DecimalMax("100.0") Double overallScore, Instant calculatedAt) {
        this.id = id;
        this.vendor = vendor;
        this.onTimePercentage = onTimePercentage;
        this.qualityCompliancePercentage = qualityCompliancePercentage;
        this.overallScore = overallScore;
        this.calculatedAt = calculatedAt;
    }
}
