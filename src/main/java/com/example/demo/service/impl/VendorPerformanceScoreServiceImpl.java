package com.example.demo.service.impl;

import com.example.demo.model.VendorPerformanceScore;
import com.example.demo.model.Vendor;
import com.example.demo.model.DeliveryEvaluation;
import com.example.demo.repository.VendorPerformanceScoreRepository;
import com.example.demo.repository.DeliveryEvaluationRepository;
import com.example.demo.repository.VendorRepository;
import com.example.demo.repository.VendorTierRepository;
import com.example.demo.service.VendorPerformanceScoreService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VendorPerformanceScoreServiceImpl implements VendorPerformanceScoreService {
    private final VendorPerformanceScoreRepository vendorPerformanceScoreRepository;
    private final DeliveryEvaluationRepository deliveryEvaluationRepository;
    private final VendorRepository vendorRepository;
    private final VendorTierRepository vendorTierRepository;

    public VendorPerformanceScoreServiceImpl(VendorPerformanceScoreRepository vendorPerformanceScoreRepository,
                                           DeliveryEvaluationRepository deliveryEvaluationRepository,
                                           VendorRepository vendorRepository,
                                           VendorTierRepository vendorTierRepository) {
        this.vendorPerformanceScoreRepository = vendorPerformanceScoreRepository;
        this.deliveryEvaluationRepository = deliveryEvaluationRepository;
        this.vendorRepository = vendorRepository;
        this.vendorTierRepository = vendorTierRepository;
    }

    @Override
    public VendorPerformanceScore calculateScore(Long vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
            .orElseThrow(() -> new RuntimeException("Vendor not found"));

        List<DeliveryEvaluation> evaluations = deliveryEvaluationRepository.findByVendorId(vendorId);

        double onTimePercentage = 0.0;
        double qualityCompliancePercentage = 0.0;

        if (!evaluations.isEmpty()) {
            long onTimeCount = evaluations.stream().filter(DeliveryEvaluation::getMeetsDeliveryTarget).count();
            long qualityCount = evaluations.stream().filter(DeliveryEvaluation::getMeetsQualityTarget).count();
            
            onTimePercentage = (onTimeCount * 100.0) / evaluations.size();
            qualityCompliancePercentage = (qualityCount * 100.0) / evaluations.size();
        }

        double overallScore = (onTimePercentage * 0.5) + (qualityCompliancePercentage * 0.5);

        VendorPerformanceScore score = new VendorPerformanceScore();
        score.setVendor(vendor);
        score.setOnTimePercentage(onTimePercentage);
        score.setQualityCompliancePercentage(qualityCompliancePercentage);
        score.setOverallScore(overallScore);
        score.setCalculatedAt(LocalDateTime.now());

        return vendorPerformanceScoreRepository.save(score);
    }

    @Override
    public VendorPerformanceScore getLatestScore(Long vendorId) {
        List<VendorPerformanceScore> scores = vendorPerformanceScoreRepository.findByVendorIdOrderByCalculatedAtDesc(vendorId);
        if (scores.isEmpty()) {
            throw new RuntimeException("No scores found for vendor");
        }
        return scores.get(0);
    }

    @Override
    public List<VendorPerformanceScore> getScoresForVendor(Long vendorId) {
        return vendorPerformanceScoreRepository.findByVendorIdOrderByCalculatedAtDesc(vendorId);
    }
}