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
        this.overallScore =
                (onTimePercentage * ON_TIME_WEIGHT) +
                (qualityCompliancePercentage * QUALITY_WEIGHT);

        this.calculatedAt = Instant.now();
    }

}
