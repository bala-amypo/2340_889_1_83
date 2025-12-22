package com.example.demo.service;

import com.example.demo.model.VendorPerformanceScore;
import java.util.List;

public interface VendorPerformanceScoreService {
    VendorPerformanceScore calculateScore(Long vendorId);
    VendorPerformanceScore getLatestScore(Long vendorId);
    List<VendorPerformanceScore> getScoresForVendor(Long vendorId);
}
