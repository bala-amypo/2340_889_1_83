package com.example.demo.controller;

import com.example.demo.model.SLARequirement;
import com.example.demo.service.SLARequirementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sla-requirements")
public class SLARequirementController {

    private final SLARequirementService service;

    public SLARequirementController(SLARequirementService service) {
        this.service = service;
    }

    // POST /api/sla-requirements
    @PostMapping
    public SLARequirement create(@RequestBody SLARequirement req) {
        return service.createRequirement(req);
    }

    // PUT /api/sla-requirements/{id}
    @PutMapping("/{id}")
    public SLARequirement update(@PathVariable Long id, @RequestBody SLARequirement req) {
        return service.updateRequirement(id, req);
    }

    // GET /api/sla-requirements/{id}
    @GetMapping("/{id}")
    public SLARequirement getById(@PathVariable Long id) {
        return service.getRequirementById(id);
    }

    // GET /api/sla-requirements
    @GetMapping
    public List<SLARequirement> getAll() {
        return service.getAllRequirements();
    }

    // PUT /api/sla-requirements/{id}/deactivate
    @PutMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Long id) {
        service.deactivateRequirement(id);
    }
}
