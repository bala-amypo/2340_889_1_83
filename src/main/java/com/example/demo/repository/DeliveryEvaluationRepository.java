package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.DeliveryEvaluation;
public interface DeliveryEvaluationRepository extends JpaRepository<DeliveryEvaluation,Long>{
    
}