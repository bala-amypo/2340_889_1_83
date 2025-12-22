@Service
public class SLARequirementServiceImpl implements SLARequirementService {

    private final SLARequirementRepository repository;

    public SLARequirementServiceImpl(SLARequirementRepository repository) {
        this.repository = repository;
    }

    @Override
    public SLARequirement createRequirement(SLARequirement req) {
        validateRequirement(req);

        req.setActive(true);
        return repository.save(req);
    }

    @Override
    public SLARequirement updateRequirement(Long id, SLARequirement req) {
        SLARequirement existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "SLA Requirement not found with id: " + id));

        if (!existing.getName().equals(req.getName())
                && repository.existsByName(req.getName())) {
            throw new DuplicateResourceException(
                    "SLA Requirement with name '" + req.getName() + "' already exists");
        }

        validateRequirement(req);

        existing.setName(req.getName());
        existing.setDays(req.getDays());
        existing.setScore(req.getScore());

        return repository.save(existing);
    }

    @Override
    public SLARequirement getRequirementById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "SLA Requirement not found with id: " + id));
    }

    @Override
    public List<SLARequirement> getAllRequirements() {
        return repository.findAll();
    }

    @Override
    public void deactivateRequirement(Long id) {
        SLARequirement req = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "SLA Requirement not found with id: " + id));

        req.setActive(false);
        repository.save(req);
    }

    private void validateRequirement(SLARequirement req) {
        if (req.getDays() <= 0) {
            throw new IllegalArgumentException("Days must be greater than 0");
        }

        if (req.getScore() < 0 || req.getScore() > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }
    }
}
