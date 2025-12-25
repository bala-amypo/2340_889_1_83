package com.example.demo.service.impl;

import com.example.demo.model.SLARequirement;
import com.example.demo.repository.SLARequirementRepository;
import com.example.demo.service.SLARequirementService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SLARequirementServiceImpl implements SLARequirementService {

    private final SLARequirementRepository repository;

    public SLARequirementServiceImpl(SLARequirementRepository repository) {
        this.repository = repository;
    }

    @Override
    public SLARequirement createRequirement(SLARequirement req) {
        if (req.getMaxDeliveryDays() == null || req.getMaxDeliveryDays() <= 0) {
            throw new IllegalArgumentException("Max delivery days");
        }
        if (req.getMinQualityScore() < 0 || req.getMinQualityScore() > 100) {
            throw new IllegalArgumentException("Quality score");
        }
        if (repository.existsByRequirementName(req.getRequirementName())) {
            throw new IllegalArgumentException("unique");
        }
        return repository.save(req);
    }

    @Override
    public SLARequirement updateRequirement(Long id, SLARequirement req) {
        SLARequirement existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found"));

        if (req.getRequirementName() != null &&
                !req.getRequirementName().equals(existing.getRequirementName()) &&
                repository.existsByRequirementName(req.getRequirementName())) {
            throw new IllegalArgumentException("unique");
        }

        if (req.getRequirementName() != null)
            existing.setRequirementName(req.getRequirementName());
        if (req.getDescription() != null)
            existing.setDescription(req.getDescription());

        return repository.save(existing);
    }

    @Override
    public SLARequirement getRequirementById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found"));
    }

    @Override
    public List<SLARequirement> getAllRequirements() {
        return repository.findAll();
    }

    @Override
    public void deactivateRequirement(Long id) {
        SLARequirement req = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found"));
        req.setActive(false);
        repository.save(req);
    }
}
