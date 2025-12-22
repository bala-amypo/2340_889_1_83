package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vendor_performance_scores")
public class VendorPerformanceScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @Column(name = "on_time_percentage", nullable = false)
    private Double onTimePercentage;

    @Column(name = "quality_compliance_percentage", nullable = false)
    private Double qualityCompliancePercentage;

    @Column(name = "overall_score", nullable = false)
    private Double overallScore;

    @Column(name = "calculated_at", nullable = false)
    private LocalDateTime calculatedAt;

    public VendorPerformanceScore() {}

    public VendorPerformanceScore(Vendor vendor, Double onTimePercentage, Double qualityCompliancePercentage, Double overallScore) {
        this.vendor = vendor;
        this.onTimePercentage = onTimePercentage;
        this.qualityCompliancePercentage = qualityCompliancePercentage;
        this.overallScore = overallScore;
        this.calculatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Vendor getVendor() { return vendor; }
    public void setVendor(Vendor vendor) { this.vendor = vendor; }

    public Double getOnTimePercentage() { return onTimePercentage; }
    public void setOnTimePercentage(Double onTimePercentage) { this.onTimePercentage = onTimePercentage; }

    public Double getQualityCompliancePercentage() { return qualityCompliancePercentage; }
    public void setQualityCompliancePercentage(Double qualityCompliancePercentage) { this.qualityCompliancePercentage = qualityCompliancePercentage; }

    public Double getOverallScore() { return overallScore; }
    public void setOverallScore(Double overallScore) { this.overallScore = overallScore; }

    public LocalDateTime getCalculatedAt() { return calculatedAt; }
    public void setCalculatedAt(LocalDateTime calculatedAt) { this.calculatedAt = calculatedAt; }
}