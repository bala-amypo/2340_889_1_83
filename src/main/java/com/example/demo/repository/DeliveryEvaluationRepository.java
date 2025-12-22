package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.DeliveryEvaluation;
public interface DeliveryEvaluationRepository extends JpaRepository<DeliveryEvaluation,Long>{
     List<DeliveryEvaluation> findByVendorId(Long vendorId);
    List<DeliveryEvaluation> findBySlaRequirementId(Long slaId);

    @Query("select d from DeliveryEvaluation d where d.vendor = :vendor and d.qualityScore >= :minScore")
    List<DeliveryEvaluation> findHighQualityDeliveries(Vendor vendor, Double minScore);

    @Query("select d from DeliveryEvaluation d where d.slaRequirement = :sla and d.meetsDeliveryTarget = true")
    List<DeliveryEvaluation> findOnTimeDeliveries(SLARequirement sla);
}