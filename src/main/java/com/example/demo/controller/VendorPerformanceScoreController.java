package com.example.demo.controller;

import com.example.demo.model.VendorPerformanceScore;
import com.example.demo.service.VendorPerformanceScoreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scores")
public class VendorPerformanceScoreController {

    private final VendorPerformanceScoreService service;

    public VendorPerformanceScoreController(VendorPerformanceScoreService service) {
        this.service = service;
    }

    // POST /api/scores/calculate/{vendorId}
    @PostMapping("/calculate/{vendorId}")
    public VendorPerformanceScore calculate(@PathVariable Long vendorId) {
        return service.calculateScore(vendorId);
    }

    // GET /api/scores/latest/{vendorId}
    @GetMapping("/latest/{vendorId}")
    public VendorPerformanceScore getLatest(@PathVariable Long vendorId) {
        return service.getLatestScore(vendorId);
    }

    // GET /api/scores/vendor/{vendorId}
    @GetMapping("/vendor/{vendorId}")
    public List<VendorPerformanceScore> getHistory(@PathVariable Long vendorId) {
        return service.getScoresForVendor(vendorId);
    }
}
