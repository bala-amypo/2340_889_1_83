package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "delivery_evaluations")
public class DeliveryEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "sla_requirement_id", nullable = false)
    private SlaRequirement slaRequirement;

    @Min(0)
    @Column(nullable = false)
    private Integer actualDeliveryDays;

    @DecimalMin("0.0")
    @DecimalMax("100.0")
    @Column(nullable = false)
    private Double qualityScore;

    @Column(nullable = false)
    private LocalDate evaluationDate;

    @Column(nullable = false)
    private Boolean meetsDeliveryTarget;

    @Column(nullable = false)
    private Boolean meetsQualityTarget;

    protected DeliveryEvaluation() {}

    public DeliveryEvaluation(Vendor vendor,SlaRequirement slaRequirement,Integer actualDeliveryDays,Double qualityScore,LocalDate evaluationDate) {
        this.vendor = vendor;
        this.slaRequirement = slaRequirement;
        this.actualDeliveryDays = actualDeliveryDays;
        this.qualityScore = qualityScore;
        this.evaluationDate = evaluationDate;
    }

    @PrePersist
    @PreUpdate
    protected void evaluateTargets() {
        if (slaRequirement != null) {
            this.meetsDeliveryTarget =
                    actualDeliveryDays <= slaRequirement.getMaxDeliveryDays();

            this.meetsQualityTarget =
                qualityScore >= slaRequirement.getMinQualityScore();
        }
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
    public SlaRequirement getSlaRequirement() {
        return slaRequirement;
    }
    public void setSlaRequirement(SlaRequirement slaRequirement) {
        this.slaRequirement = slaRequirement;
    }
    public Integer getActualDeliveryDays() {
        return actualDeliveryDays;
    }
    public void setActualDeliveryDays(Integer actualDeliveryDays) {
        this.actualDeliveryDays = actualDeliveryDays;
    }

    public Double getQualityScore() {
        return qualityScore;
    }
    public void setQualityScore(Double qualityScore) {
        this.qualityScore = qualityScore;
    }
    public LocalDate getEvaluationDate() {
        return evaluationDate;
    }
    public void setEvaluationDate(LocalDate evaluationDate) {
        this.evaluationDate = evaluationDate;
    }
    public Boolean getMeetsDeliveryTarget() {
        return meetsDeliveryTarget;
    }
    public void setMeetsDeliveryTarget(Boolean meetsDeliveryTarget) {
        this.meetsDeliveryTarget = meetsDeliveryTarget;
    }
    public Boolean getMeetsQualityTarget() {
        return meetsQualityTarget;
    }
    public void setMeetsQualityTarget(Boolean meetsQualityTarget) {
        this.meetsQualityTarget = meetsQualityTarget;
    }
}
