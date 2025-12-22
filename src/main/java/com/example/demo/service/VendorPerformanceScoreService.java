public interface VendorPerformanceScoreService {

    VendorPerformanceScore calculateScore(Long vendorId);

    VendorPerformanceScore getLatestScore(Long vendorId);

    List<VendorPerformanceScore> getScoresForVendor(Long vendorId);
}
