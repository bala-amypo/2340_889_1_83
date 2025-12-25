package com.example.demo.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "vendor_performance_scores")
public class VendorPerformanceScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Vendor vendor;

    private Double onTimePercentage;
    private Double qualityCompliancePercentage;
    private Double overallScore;
    private Timestamp calculatedAt;

    public VendorPerformanceScore() {}

    public VendorPerformanceScore(Vendor vendor,
                                  Double onTimePercentage,
                                  Double qualityCompliancePercentage,
                                  Double overallScore) {
        this.vendor = vendor;
        this.onTimePercentage = onTimePercentage;
        this.qualityCompliancePercentage = qualityCompliancePercentage;
        this.overallScore = overallScore;
        this.calculatedAt = new Timestamp(System.currentTimeMillis());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Vendor getVendor() { return vendor; }
    public void setVendor(Vendor vendor) { this.vendor = vendor; }

    public Double getOnTimePercentage() { return onTimePercentage; }
    public void setOnTimePercentage(Double onTimePercentage) { this.onTimePercentage = onTimePercentage; }
