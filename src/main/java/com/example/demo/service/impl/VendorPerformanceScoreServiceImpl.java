@Service
public class VendorPerformanceScoreServiceImpl implements VendorPerformanceScoreService {

    private final VendorRepository vendorRepository;
    private final DeliveryEvaluationRepository evaluationRepository;
    private final VendorPerformanceScoreRepository scoreRepository;

    public VendorPerformanceScoreServiceImpl(
            VendorRepository vendorRepository,
            DeliveryEvaluationRepository evaluationRepository,
            VendorPerformanceScoreRepository scoreRepository) {
        this.vendorRepository = vendorRepository;
        this.evaluationRepository = evaluationRepository;
        this.scoreRepository = scoreRepository;
    }

    @Override
    public VendorPerformanceScore calculateScore(Long vendorId) {

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vendor not found with id: " + vendorId));

        List<DeliveryEvaluation> evaluations =
                evaluationRepository.findByVendorId(vendorId);

        if (evaluations.isEmpty()) {
            throw new IllegalStateException(
                    "No delivery evaluations found for vendor");
        }

        long total = evaluations.size();

        long onTimeCount = evaluations.stream()
                .filter(DeliveryEvaluation::isMeetsDeliveryTarget)
                .count();

        long qualityCount = evaluations.stream()
                .filter(DeliveryEvaluation::isMeetsQualityTarget)
                .count();

        double onTimePercentage =
                (onTimeCount * 100.0) / total;

        double qualityCompliancePercentage =
                (qualityCou
