package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.VendorPerformanceScoreService;

import java.sql.Timestamp;
import java.util.List;

public class VendorPerformanceScoreServiceImpl implements VendorPerformanceScoreService {

    private final VendorPerformanceScoreRepository scoreRepository;
    private final DeliveryEvaluationRepository evaluationRepository;
    private final VendorRepository vendorRepository;
    private final VendorTierRepository tierRepository;

    public VendorPerformanceScoreServiceImpl(
            VendorPerformanceScoreRepository scoreRepository,
            DeliveryEvaluationRepository evaluationRepository,
            VendorRepository vendorRepository,
            VendorTierRepository tierRepository) {
        this.scoreRepository = scoreRepository;
        this.evaluationRepository = evaluationRepository;
        this.vendorRepository = vendorRepository;
        this.tierRepository = tierRepository;
    }

    @Override
    public VendorPerformanceScore calculateScore(Long vendorId) {

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        List<DeliveryEvaluation> evaluations =
                evaluationRepository.findByVendorId(vendorId);

        double total = evaluations.size();
        double onTime = evaluations.stream()
                .filter(DeliveryEvaluation::getMeetsDeliveryTarget)
                .count();
        double quality = evaluations.stream()
                .filter(DeliveryEvaluation::getMeetsQualityTarget)
                .count();

        double onTimePercentage = total == 0 ? 0 : (onTime / total) * 100;
        double qualityPercentage = total == 0 ? 0 : (quality / total) * 100;
        double overallScore = (onTimePercentage + qualityPercentage) / 2;

        VendorPerformanceScore score = new VendorPerformanceScore();
        score.setVendor(vendor);
        score.setOnTimePercentage(onTimePercentage);
        score.setQualityCompliancePercentage(qualityPercentage);
        score.setOverallScore(overallScore);
        score.setCalculatedAt(new Timestamp(System.currentTimeMillis()));

        return scoreRepository.save(score);
    }

    @Override
    public VendorPerformanceScore getLatestScore(Long vendorId) {
        List<VendorPerformanceScore> scores =
                scoreRepository.findByVendorOrderByCalculatedAtDesc(vendorId);
        return scores.isEmpty() ? null : scores.get(0);
    }

    @Override
    public List<VendorPerformanceScore> getScoresForVendor(Long vendorId) {
        return scoreRepository.findByVendorOrderByCalculatedAtDesc(vendorId);
    }
}
