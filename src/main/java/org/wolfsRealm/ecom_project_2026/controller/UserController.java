package org.wolfsRealm.ecom_project_2026.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wolfsRealm.ecom_project_2026.config.AppConstants;
import org.wolfsRealm.ecom_project_2026.payload.UserDTO;
import org.wolfsRealm.ecom_project_2026.payload.UserRequestDTO;
import org.wolfsRealm.ecom_project_2026.payload.UserResponse;
import org.wolfsRealm.ecom_project_2026.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }

    @GetMapping("/users")
    public ResponseEntity<UserResponse>getAllUsers(
            @RequestParam(name="pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)Integer pageNumber,
            @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
            @RequestParam(name="sortBy",defaultValue = AppConstants.USER_SORT_BY,required=false)String sortBy,
            @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_ORDER,required = false)String sortOrder
    ){
        return new ResponseEntity<>(userService.getAllUsers(pageNumber,pageSize,sortBy,sortOrder), HttpStatus.ACCEPTED);
    }

    @PutMapping("/users")
    public ResponseEntity<UserDTO>updateUserDetails(@Valid @RequestBody UserRequestDTO userRequestDTO){
        return new ResponseEntity<>(userService.updateUserDetails(userRequestDTO),HttpStatus.ACCEPTED);

    }
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUserByUserId(@PathVariable Long userId){
        return new ResponseEntity<>(userService.deleteUserById(userId),HttpStatus.OK);
    }
}
