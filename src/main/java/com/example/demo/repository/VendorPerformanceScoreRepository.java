package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.VendorPerformanceScore;
public interface VendorPerformanceScoreRepository extends JpaRepository<VendorPerformanceScore,Long>{
    
}