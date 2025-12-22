package com.example.demo.service.impl;

import com.example.demo.model.VendorTier;
import com.example.demo.repository.VendorTierRepository;
import com.example.demo.service.VendorTierService;

import java.util.List;

public class VendorTierServiceImpl implements VendorTierService {

    private final VendorTierRepository repository;

    public VendorTierServiceImpl(VendorTierRepository repository) {
        this.repository = repository;
    }

    @Override
    public VendorTier createTier(VendorTier tier) {
        if (tier.getMinScoreThreshold() < 0 || tier.getMinScoreThreshold() > 100) {
            throw new IllegalArgumentException("Score threshold must be between 0–100");
        }
        if (repository.existsByTierName(tier.getTierName())) {
            throw new IllegalArgumentException("Tier name must be unique");
        }
        return repository.save(tier);
    }

    @Override
    public VendorTier updateTier(Long id, VendorTier tier) {
        VendorTier existing = getTierById(id);
        existing.setTierName(tier.getTierName());
        existing.setDescription(tier.getDescription());
        existing.setMinScoreThreshold(tier.getMinScoreThreshold());
        return repository.save(existing);
    }

    @Override
    public VendorTier getTierById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tier not found"));
    }

    @Override
    public List<VendorTier> getAllTiers() {
        return repository.findAll();
    }

    @Override
    public void deactivateTier(Long id) {
        VendorTier tier = getTierById(id);
        tier.setActive(false);
        repository.save(tier);
    }
}
