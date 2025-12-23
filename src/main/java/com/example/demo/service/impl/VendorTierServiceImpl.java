package com.example.demo.service.impl;

import com.example.demo.model.VendorTier;
import com.example.demo.repository.VendorTierRepository;
import com.example.demo.service.VendorTierService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class VendorTierServiceImpl implements VendorTierService {
    private final VendorTierRepository vendorTierRepository;

    public VendorTierServiceImpl(VendorTierRepository vendorTierRepository) {
        this.vendorTierRepository = vendorTierRepository;
    }

    @Override
    @Transactional
    public VendorTier createTier(VendorTier tier) {
        if (tier.getMinScoreThreshold() < 0 || tier.getMinScoreThreshold() > 100) {
            throw new IllegalArgumentException("Min score threshold must be between 0–100");
        }
        if (vendorTierRepository.existsByTierName(tier.getTierName())) {
            throw new IllegalArgumentException("Tier name must be unique");
        }
        return vendorTierRepository.save(tier);
    }

    @Override
    @Transactional
    public VendorTier updateTier(Long id, VendorTier tier) {
        VendorTier existing = getTierById(id);
        existing.setTierName(tier.getTierName());
        existing.setMinScoreThreshold(tier.getMinScoreThreshold());
        existing.setDescription(tier.getDescription());
        existing.setActive(tier.getActive());
        return vendorTierRepository.save(existing);
    }

    @Override
    public VendorTier getTierById(Long id) {
        return vendorTierRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Vendor tier not found"));
    }

    @Override
    public List<VendorTier> getAllTiers() {
        return vendorTierRepository.findAll();
    }

    @Override
    @Transactional
    public void deactivateTier(Long id) {
        VendorTier tier = getTierById(id);
        tier.setActive(false);
        vendorTierRepository.save(tier);
    }
}