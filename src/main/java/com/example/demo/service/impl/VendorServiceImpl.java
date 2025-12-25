package com.example.demo.service.impl;

import com.example.demo.model.Vendor;
import com.example.demo.repository.VendorRepository;
import com.example.demo.service.VendorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    public VendorServiceImpl(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    public Vendor createVendor(Vendor vendor) {
        if (vendorRepository.existsByName(vendor.getName())) {
            throw new IllegalArgumentException("unique");
        }
        vendor.setActive(true);
        return vendorRepository.save(vendor);
    }

    @Override
    public Vendor updateVendor(Long id, Vendor vendor) {
        Vendor existing = vendorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found"));

        if (vendor.getName() != null &&
                !vendor.getName().equals(existing.getName()) &&
                vendorRepository.existsByName(vendor.getName())) {
            throw new IllegalArgumentException("unique");
        }

        if (vendor.getContactEmail() != null)
            existing.setContactEmail(vendor.getContactEmail());
        if (vendor.getContactPhone() != null)
            existing.setContactPhone(vendor.getContactPhone());

        return vendorRepository.save(existing);
    }

    @Override
    public Vendor getVendorById(Long id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found"));
    }

    @Override
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    @Override
    public void deactivateVendor(Long id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found"));
        vendor.setActive(false);
        vendorRepository.save(vendor);
    }
}
