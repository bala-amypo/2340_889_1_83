package com.example.demo.model;

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
}
