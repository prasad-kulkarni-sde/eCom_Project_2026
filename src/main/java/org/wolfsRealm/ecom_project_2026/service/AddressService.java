package org.wolfsRealm.ecom_project_2026.service;

import jakarta.validation.Valid;
import org.wolfsRealm.ecom_project_2026.model.User;
import org.wolfsRealm.ecom_project_2026.payload.AddressDTO;

public interface AddressService {
    AddressDTO createAddress(@Valid AddressDTO addressDTO, User user);
}
