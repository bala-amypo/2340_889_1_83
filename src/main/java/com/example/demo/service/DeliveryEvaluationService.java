package com.example.demo.service;

import com.example.demo.model.DeliveryEvaluation;
import java.util.List;

public interface DeliveryEvaluationService {

    DeliveryEvaluation createEvaluation(DeliveryEvaluation evaluation);

    DeliveryEvaluation getEvaluationById(Long id);

    List<DeliveryEvaluation> getEvaluationsForVendor(Long vendorId);

    List<DeliveryEvaluation> getEvaluationsForRequirement(Long requirementId);
}
