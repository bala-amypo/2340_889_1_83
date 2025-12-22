package com.example.demo.controller;

import com.example.demo.model.SLARequirement;
import com.example.demo.service.SLARequirementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sla-requirements")
@Tag(name = "SLA Requirements", description = "SLA requirement management")
@SecurityRequirement(name = "Bearer Authentication")
public class SLARequirementController {
    private final SLARequirementService slaRequirementService;

    public SLARequirementController(SLARequirementService slaRequirementService) {
        this.slaRequirementService = slaRequirementService;
    }

    @PostMapping
    @Operation(summary = "Create SLA requirement")
    public ResponseEntity<SLARequirement> createRequirement(@RequestBody SLARequirement requirement) {
        return ResponseEntity.ok(slaRequirementService.createRequirement(requirement));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update SLA requirement")
    public ResponseEntity<SLARequirement> updateRequirement(@PathVariable Long id, @RequestBody SLARequirement requirement) {
        return ResponseEntity.ok(slaRequirementService.updateRequirement(id, requirement));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get SLA requirement by ID")
    public ResponseEntity<SLARequirement> getRequirement(@PathVariable Long id) {
        return ResponseEntity.ok(slaRequirementService.getRequirementById(id));
    }

    @GetMapping
    @Operation(summary = "Get all SLA requirements")
    public ResponseEntity<List<SLARequirement>> getAllRequirements() {
        return ResponseEntity.ok(slaRequirementService.getAllRequirements());
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate SLA requirement")
    public ResponseEntity<Void> deactivateRequirement(@PathVariable Long id) {
        slaRequirementService.deactivateRequirement(id);
        return ResponseEntity.ok().build();
    }
}