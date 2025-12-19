public interface VendorService {

    Vendor createVendor(Vendor vendor);

    Vendor updateVendor(Long id, Vendor vendor);

    Vendor getVendorById(Long id);

    List<Vendor> getAllVendors();

    void deactivateVendor(Long id);
}
