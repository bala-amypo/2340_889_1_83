package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "delivery_evaluations")
public class DeliveryEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sla_requirement_id", nullable = false)
    private SLARequirement slaRequirement;

    @Column(name = "actual_delivery_days", nullable = false)
    private Integer actualDeliveryDays;

    @Column(name = "quality_score", nullable = false)
    private Double qualityScore;

    @Column(name = "evaluation_date", nullable = false)
    private LocalDate evaluationDate;

    @Column(name = "meets_delivery_target", nullable = false)
    private Boolean meetsDeliveryTarget;

    @Column(name = "meets_quality_target", nullable = false)
    private Boolean meetsQualityTarget;

    public DeliveryEvaluation() {}

    public DeliveryEvaluation(Vendor vendor, SLARequirement slaRequirement, Integer actualDeliveryDays, 
                            Double qualityScore, LocalDate evaluationDate) {
        this.vendor = vendor;
        this.slaRequirement = slaRequirement;
        this.actualDeliveryDays = actualDeliveryDays;
        this.qualityScore = qualityScore;
        this.evaluationDate = evaluationDate;
        this.meetsDeliveryTarget = actualDeliveryDays <= slaRequirement.getMaxDeliveryDays();
        this.meetsQualityTarget = qualityScore >= slaRequirement.getMinQualityScore();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Vendor getVendor() { return vendor; }
    public void setVendor(Vendor vendor) { this.vendor = vendor; }

    public SLARequirement getSlaRequirement() { return slaRequirement; }
    public void setSlaRequirement(SLARequirement slaRequirement) { this.slaRequirement = slaRequirement; }

    public Integer getActualDeliveryDays() { return actualDeliveryDays; }
    public void setActualDeliveryDays(Integer actualDeliveryDays) { this.actualDeliveryDays = actualDeliveryDays; }

    public Double getQualityScore() { return qualityScore; }
    public void setQualityScore(Double qualityScore) { this.qualityScore = qualityScore; }

    public LocalDate getEvaluationDate() { return evaluationDate; }
    public void setEvaluationDate(LocalDate evaluationDate) { this.evaluationDate = evaluationDate; }

    public Boolean getMeetsDeliveryTarget() { return meetsDeliveryTarget; }
    public void setMeetsDeliveryTarget(Boolean meetsDeliveryTarget) { this.meetsDeliveryTarget = meetsDeliveryTarget; }

    public Boolean getMeetsQualityTarget() { return meetsQualityTarget; }
    public void setMeetsQualityTarget(Boolean meetsQualityTarget) { this.meetsQualityTarget = meetsQualityTarget; }
}