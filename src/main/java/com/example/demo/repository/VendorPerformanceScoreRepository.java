package com.example.demo.repository;

import com.example.demo.model.VendorPerformanceScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VendorPerformanceScoreRepository
        extends JpaRepository<VendorPerformanceScore, Long> {

    @Query("SELECT v FROM VendorPerformanceScore v " +
           "WHERE v.vendor.id = :vendorId " +
           "ORDER BY v.calculatedAt DESC")
    List<VendorPerformanceScore> findByVendorOrderByCalculatedAtDesc(
            @Param("vendorId") Long vendorId
    );
}
