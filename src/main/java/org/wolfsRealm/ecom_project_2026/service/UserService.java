package org.wolfsRealm.ecom_project_2026.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.wolfsRealm.ecom_project_2026.payload.UserDTO;
import org.wolfsRealm.ecom_project_2026.payload.UserRequestDTO;
import org.wolfsRealm.ecom_project_2026.payload.UserResponse;

public interface UserService {

    UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    UserDTO updateUserDetails(UserRequestDTO userRequestDTO);

    String deleteUserById(Long userId);
}
