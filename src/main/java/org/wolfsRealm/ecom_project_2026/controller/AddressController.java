package org.wolfsRealm.ecom_project_2026.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.wolfsRealm.ecom_project_2026.model.User;
import org.wolfsRealm.ecom_project_2026.payload.AddressDTO;
import org.wolfsRealm.ecom_project_2026.service.AddressService;
import org.wolfsRealm.ecom_project_2026.util.AuthUtil;

@RestController
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {this.addressService = addressService;}

    @Autowired
    public AuthUtil authUtil;

    @PostMapping("api/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO){
        User user= authUtil.loggedInUser();
        return new ResponseEntity<>(addressService.createAddress(addressDTO,user), HttpStatus.CREATED);
    }

}
