package com.example.demo.repository;

import com.example.demo.model.DeliveryEvaluation;
import com.example.demo.model.Vendor;
import com.example.demo.model.SLARequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DeliveryEvaluationRepository extends JpaRepository<DeliveryEvaluation, Long> {
    List<DeliveryEvaluation> findByVendorId(Long vendorId);
    List<DeliveryEvaluation> findBySlaRequirementId(Long slaId);
    
    @Query("SELECT de FROM DeliveryEvaluation de WHERE de.vendor = :vendor AND de.qualityScore >= :minScore")
    List<DeliveryEvaluation> findHighQualityDeliveries(@Param("vendor") Vendor vendor, @Param("minScore") Double minScore);
    
    @Query("SELECT de FROM DeliveryEvaluation de WHERE de.slaRequirement = :sla AND de.meetsDeliveryTarget = true")
    List<DeliveryEvaluation> findOnTimeDeliveries(@Param("sla") SLARequirement sla);
}