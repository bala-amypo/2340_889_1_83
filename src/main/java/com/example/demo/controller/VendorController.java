package com.example.demo.controller;

import com.example.demo.model.Vendor;
import com.example.demo.service.VendorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @PostMapping
    public Vendor create(@RequestBody Vendor vendor) {
        return vendorService.createVendor(vendor);
    }

    @PutMapping("/{id}")
    public Vendor update(@PathVariable Long id,
                         @RequestBody Vendor vendor) {
        return vendorService.updateVendor(id, vendor);
    }

    @GetMapping("/{id}")
    public Vendor get(@PathVariable Long id) {
        return vendorService.getVendorById(id);
    }

    @GetMapping
    public List<Vendor> list() {
        return vendorService.getAllVendors();
    }

    @PutMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Long id) {
        vendorService.deactivateVendor(id);
    }
}
