package com.example.demo.service;

import com.example.demo.model.Vendor;
import java.util.List;

public interface VendorService {

    Vendor createVendor(Vendor vendor);

    Vendor updateVendor(Long id, Vendor vendor);

    Vendor getVendorById(Long id);

    List<Vendor> getAllVendors();

    void deactivateVendor(Long id);
}
