package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.SLARequirement;
public interface SLARequirementRepository extends JpaRepository<SLARequirement,Long>{
    
}