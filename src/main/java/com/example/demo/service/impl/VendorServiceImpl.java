package com.example.demo.service.impl;

import com.example.demo.model.Vendor;
import com.example.demo.repository.VendorRepository;
import com.example.demo.service.VendorService;
import java.util.List;

@Service
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    public VendorServiceImpl(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    public Vendor createVendor(Vendor vendor) {
        if (vendorRepository.existsByNameIgnoreCase(vendor.getName())) {
            throw new RuntimeException("Vendor with this name already exists");
        }
        vendor.setActive(true);
        return vendorRepository.save(vendor);
    }

    @Override
    public Vendor updateVendor(Long id, Vendor vendor) {
        Vendor existingVendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        existingVendor.setName(vendor.getName());
        existingVendor.setEmail(vendor.getEmail());
        existingVendor.setPhone(vendor.getPhone());

        return vendorRepository.save(existingVendor);
    }

    @Override
    public Vendor getVendorById(Long id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
    }

    @Override
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    @Override
    public void deactivateVendor(Long id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        vendor.setActive(false);
        vendorRepository.save(vendor);
    }
}
