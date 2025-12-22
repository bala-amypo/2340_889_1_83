package com.example.demo.controller;

import com.example.demo.model.VendorTier;
import com.example.demo.service.VendorTierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tiers")
@Tag(name = "Vendor Tiers", description = "Vendor tier management")
@SecurityRequirement(name = "Bearer Authentication")
public class VendorTierController {
    private final VendorTierService vendorTierService;

    public VendorTierController(VendorTierService vendorTierService) {
        this.vendorTierService = vendorTierService;
    }

    @PostMapping
    @Operation(summary = "Create vendor tier")
    public ResponseEntity<VendorTier> createTier(@RequestBody VendorTier tier) {
        return ResponseEntity.ok(vendorTierService.createTier(tier));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update vendor tier")
    public ResponseEntity<VendorTier> updateTier(@PathVariable Long id, @RequestBody VendorTier tier) {
        return ResponseEntity.ok(vendorTierService.updateTier(id, tier));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tier by ID")
    public ResponseEntity<VendorTier> getTier(@PathVariable Long id) {
        return ResponseEntity.ok(vendorTierService.getTierById(id));
    }

    @GetMapping
    @Operation(summary = "Get all tiers")
    public ResponseEntity<List<VendorTier>> getAllTiers() {
        return ResponseEntity.ok(vendorTierService.getAllTiers());
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate tier")
    public ResponseEntity<Void> deactivateTier(@PathVariable Long id) {
        vendorTierService.deactivateTier(id);
        return ResponseEntity.ok().build();
    }
}