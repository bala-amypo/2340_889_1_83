package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "sla_requirements", uniqueConstraints = {
    @UniqueConstraint(columnNames = "requirement_name")
})
public class SLARequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "requirement_name", nullable = false, unique = true)
    private String requirementName;

    @Column(length = 500)
    private String description;

    @Column(name = "max_delivery_days", nullable = false)
    private Integer maxDeliveryDays;

    @Column(name = "min_quality_score", nullable = false)
    private Double minQualityScore;

    @Column(nullable = false)
    private Boolean active = true;

    @PrePersist
    protected void onCreate() {
        if (active == null) {
            active = true;
        }
    }

    public SLARequirement() {}

    public SLARequirement(String requirementName, String description, Integer maxDeliveryDays, Double minQualityScore) {
        this.requirementName = requirementName;
        this.description = description;
        this.maxDeliveryDays = maxDeliveryDays;
        this.minQualityScore = minQualityScore;
        this.active = true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRequirementName() { return requirementName; }
    public void setRequirementName(String requirementName) { this.requirementName = requirementName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getMaxDeliveryDays() { return maxDeliveryDays; }
    public void setMaxDeliveryDays(Integer maxDeliveryDays) { this.maxDeliveryDays = maxDeliveryDays; }

    public Double getMinQualityScore() { return minQualityScore; }
    public void setMinQualityScore(Double minQualityScore) { this.minQualityScore = minQualityScore; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}