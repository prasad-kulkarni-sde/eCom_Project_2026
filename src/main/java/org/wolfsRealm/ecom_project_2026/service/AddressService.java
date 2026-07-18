package org.wolfsRealm.ecom_project_2026.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.wolfsRealm.ecom_project_2026.model.User;
import org.wolfsRealm.ecom_project_2026.payload.AddressDTO;
import org.wolfsRealm.ecom_project_2026.payload.AddressResponse;

public interface AddressService {
    AddressDTO createAddress(@Valid AddressDTO addressDTO, User user);

    AddressResponse getAddresses(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    AddressDTO getAddressById(Long addressId);

    AddressResponse getAddressesByUser(User user, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    @Transactional
    AddressDTO updateAddress(Long addressId, @Valid AddressDTO addressDTO);

    @Transactional
    String deleteAddress(Long addressId);
}
