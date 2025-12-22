public interface VendorTierService {

    VendorTier createTier(VendorTier tier);

    VendorTier updateTier(Long id, VendorTier tier);

    VendorTier getTierById(Long id);

    List<VendorTier> getAllTiers();

    void deactivateTier(Long id);
}
