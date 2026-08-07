package org.wolfsRealm.ecom_project_2026.service;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.wolfsRealm.ecom_project_2026.exceptions.APIException;
import org.wolfsRealm.ecom_project_2026.exceptions.ResourceNotFoundException;
import org.wolfsRealm.ecom_project_2026.model.User;

import org.wolfsRealm.ecom_project_2026.payload.UserDTO;
import org.wolfsRealm.ecom_project_2026.payload.UserRequestDTO;
import org.wolfsRealm.ecom_project_2026.payload.UserResponse;
import org.wolfsRealm.ecom_project_2026.repositories.UserRepository;
import org.wolfsRealm.ecom_project_2026.util.AuthUtil;

import java.util.List;


@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthUtil authUtil;


    @Override
    public UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder= sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<User> UserPage = userRepository.findAll(pageDetails);

        List<User> list= UserPage.getContent();
        if (list.isEmpty())throw new APIException("No User Found!!!");

        UserResponse userResponse= new UserResponse();
        List<UserDTO> userDTOS = list.stream().map(user -> {
            UserDTO dto = modelMapper.map(user, UserDTO.class);
            List<String> roles = user.getRoles().stream()
                    .map(role -> role.getRoleName().name())
                    .toList();
            dto.setRoles(roles);
            return dto;
        }).toList();

        userResponse.setContent(userDTOS);
        userResponse.setPageNumber(UserPage.getNumber());
        userResponse.setTotalPages(UserPage.getTotalPages());
        userResponse.setTotalElements(UserPage.getTotalElements());
        userResponse.setPageSize(UserPage.getSize());
        userResponse.setLast(UserPage.isLast());

        return userResponse;
    }

    @Override
    public UserDTO updateUserDetails( UserRequestDTO userRequestDTO) {
        User user= authUtil.loggedInUser();
        String email= userRequestDTO.getEmail();
        if (email==null)throw new APIException("Email ID not Provided !!");
        user.setEmail(email);
        userRepository.save(user);
        return modelMapper.map(user, UserDTO.class);

    }

    @Override
    public String deleteUserById(Long userId) {
        User user= userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","userId",userId));
        userRepository.delete(user);
        return "User Deleted Successfully !!";
    }

}
