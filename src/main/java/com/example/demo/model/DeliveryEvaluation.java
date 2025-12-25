package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "delivery_evaluations")
public class DeliveryEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Vendor vendor;

    @ManyToOne
    private SLARequirement slaRequirement;

    private Integer actualDeliveryDays;
    private Double qualityScore;
    private LocalDate evaluationDate;
    private Boolean meetsDeliveryTarget;
    private Boolean meetsQualityTarget;

    public DeliveryEvaluation() {}

    public DeliveryEvaluation(Vendor vendor, SLARequirement slaRequirement,
                              Integer actualDeliveryDays, Double qualityScore,
                              LocalDate evaluationDate) {
        this.vendor = vendor;
        this.slaRequirement = slaRequirement;
        this.actualDeliveryDays = actualDeliveryDays;
        this.qualityScore = qualityScore;
        this.evaluationDate = evaluationDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.
