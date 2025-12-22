@Service
public class DeliveryEvaluationServiceImpl implements DeliveryEvaluationService {

    private final DeliveryEvaluationRepository evaluationRepository;
    private final VendorRepository vendorRepository;
    private final SLARequirementRepository requirementRepository;

    public DeliveryEvaluationServiceImpl(
            DeliveryEvaluationRepository evaluationRepository,
            VendorRepository vendorRepository,
            SLARequirementRepository requirementRepository) {
        this.evaluationRepository = evaluationRepository;
        this.vendorRepository = vendorRepository;
        this.requirementRepository = requirementRepository;
    }

    @Override
    public DeliveryEvaluation createEvaluation(DeliveryEvaluation evaluation) {

        Vendor vendor = vendorRepository.findById(
                evaluation.getVendor().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vendor not found"));

        if (!vendor.isActive()) {
            throw new IllegalStateException("Only active vendors can be evaluated");
        }

        SLARequirement requirement = requirementRepository.findById(
                evaluation.getRequirement().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "SLA Requirement not found"));

        if (evaluation.getDaysTaken() < 0) {
            throw new IllegalArgumentException("Days must be greater than or equal to 0");
        }

        if (evaluation.getScore() < 0 || evaluation.getScore() > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }

        boolean meetsDeliveryTarget =
                evaluation.getDaysTaken() <= requirement.getDays();

        boolean meetsQualityTarget =
                evaluation.getScore() >= requirement.getScore();

        evaluation.setVendor(vendor);
        evaluation.setRequirement(requirement);
        evaluation.setMeetsDeliveryTarget(meetsDeliveryTarget);
        evaluation.setMeetsQualityTarget(meetsQualityTarget);

        return evaluationRepository.save(evaluation);
    }

    @Override
    public DeliveryEvaluation getEvaluationById(Long id) {
        return evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Delivery Evaluation not found with id: " + id));
    }

    @Override
    public List<DeliveryEvaluation> getEvaluationsForVendor(Long vendorId) {
        if (!vendorRepository.existsById(vendorId)) {
            throw new ResourceNotFoundException(
