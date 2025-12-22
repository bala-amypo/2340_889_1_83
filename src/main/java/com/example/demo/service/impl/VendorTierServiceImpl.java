@Service
public class VendorTierServiceImpl implements VendorTierService {

    private final VendorTierRepository tierRepository;

    public VendorTierServiceImpl(VendorTierRepository tierRepository) {
        this.tierRepository = tierRepository;
    }

    @Override
    public VendorTier createTier(VendorTier tier) {
        validateThreshold(tier.getThreshold());

        tier.setActive(true);
        return tierRepository.save(tier);
    }

    @Override
    public VendorTier updateTier(Long id, VendorTier tier) {
        VendorTier existing = tierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vendor Tier not found with id: " + id));

        validateThreshold(tier.getThreshold());

        existing.setName(tier.getName());
        existing.setThreshold(tier.getThreshold());

        return tierRepository.save(existing);
    }

    @Override
    public VendorTier getTierById(Long id) {
        return tierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vendor Tier not found with id: " + id));
    }

    @Override
    public List<VendorTier> getAllTiers() {
        return tierRepository.findAll();
    }

    @Override
    public void deactivateTier(Long id) {
        VendorTier tier = tierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vendor Tier not found with id: " + id));

        tier.setActive(false);
        tierRepository.save(tier);
    }

    private void validateThreshold(int threshold) {
        if (threshold < 0 || threshold > 100) {
            throw new IllegalArgumentException(
                    "Tier threshold must be between 0 and 100");
        }
    }
}
