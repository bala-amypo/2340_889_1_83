package com.example.demo.repository;

import com.example.demo.model.VendorTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VendorTierRepository extends JpaRepository<VendorTier, Long> {
    List<VendorTier> findByActiveTrueOrderByMinScoreThresholdDesc();
    boolean existsByTierName(String name);
}