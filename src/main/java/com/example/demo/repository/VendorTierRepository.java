package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.VendorTier;
public interface VendorTierRepository extends JpaRepository<VendorTier,Long>{
    
}