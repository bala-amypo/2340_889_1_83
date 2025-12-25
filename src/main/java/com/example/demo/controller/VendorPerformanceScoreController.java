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

    @PostMapping("/calculate/{vendorId}")
    public VendorPerformanceScore calculate(@PathVariable Long vendorId) {
        return service.calculateScore(vendorId);
    }

    @GetMapping("/latest/{vendorId}")
    public VendorPerformanceScore latest(@PathVariable Long vendorId) {
        return service.getLatestScore(vendorId);
    }

    @GetMapping("/vendor/{vendorId}")
    public List<VendorPerformanceScore> history(@PathVariable Long vendorId) {
        return service.getScoresForVendor(vendorId);
    }
}
