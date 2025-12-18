package com.example.demo.model;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
@Entity
@Table(
    name = "sla_requirements",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "requirement_name")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SLARequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "requirement_name", nullable = false, unique = true)
    private String requirementName;
    @Column(length = 500)
    private String description;
    @Column(nullable = false)
    @Min(value = 1, message = "Max delivery days must be greater than 0")
    private Integer maxDeliveryDays;
    @Column(nullable = false)
    @DecimalMin(value = "0.0", inclusive = true, message = "Minimum quality score must be >= 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "Minimum quality score must be <= 100")
    private Double minQualityScore;
    @Column(nullable = false)
    private Boolean active = true;
    @PrePersist
    public void prePersist() {
        if (active == null) {
            active = true;
        }
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getRequirementName() {
        return requirementName;
    }
    public void setRequirementName(String requirementName) {
        this.requirementName = requirementName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getMaxDeliveryDays() {
        return maxDeliveryDays;
    }
    public void setMaxDeliveryDays(Integer maxDeliveryDays) {
        this.maxDeliveryDays = maxDeliveryDays;
    }
    public Double getMinQualityScore() {
        return minQualityScore;
    }
    public void setMinQualityScore(Double minQualityScore) {
        this.minQualityScore = minQualityScore;
    }
    public Boolean getActive() {
        return active;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
}
