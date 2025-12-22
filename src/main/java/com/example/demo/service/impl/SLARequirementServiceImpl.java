package com.example.demo.service.impl;

import com.example.demo.model.SLARequirement;
import com.example.demo.repository.SLARequirementRepository;
import com.example.demo.service.SLARequirementService;

import java.util.List;

public class SLARequirementServiceImpl implements SLARequirementService {

    private final SLARequirementRepository repository;

    public SLARequirementServiceImpl(SLARequirementRepository repository) {
        this.repository = repository;
    }

    @Override
    public SLARequirement createRequirement(SLARequirement req) {
        if (req.getMaxDeliveryDays() == null || req.getMaxDeliveryDays() <= 0) {
            throw new IllegalArgumentException("Max delivery days must be greater than 0");
        }
        if (req.getMinQualityScore() < 0 || req.getMinQualityScore() > 100) {
            throw new IllegalArgumentException("Quality score must be between 0 and 100");
        }
        return repository.save(req);
    }

    @Override
    public SLARequirement updateRequirement(Long id, SLARequirement req) {
        SLARequirement existing = getRequirementById(id);

        if (!existing.getRequirementName().equals(req.getRequirementName()) &&
                repository.existsByRequirementName(req.getRequirementName())) {
            throw new IllegalArgumentException("Requirement name must be unique");
        }

        existing.setRequirementName(req.getRequirementName());
        existing.setDescription(req.getDescription());
        existing.setMaxDeliveryDays(req.getMaxDeliveryDays());
        existing.setMinQualityScore(req.getMinQualityScore());

        return repository.save(existing);
    }

    @Override
    public SLARequirement getRequirementById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SLA requirement not found"));
    }

    @Override
    public List<SLARequirement> getAllRequirements() {
        return repository.findAll();
    }

    @Override
    public void deactivateRequirement(Long id) {
        SLARequirement req = getRequirementById(id);
        req.setActive(false);
        repository.save(req);
    }
}
