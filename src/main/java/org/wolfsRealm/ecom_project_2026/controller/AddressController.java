package org.wolfsRealm.ecom_project_2026.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wolfsRealm.ecom_project_2026.config.AppConstants;
import org.wolfsRealm.ecom_project_2026.model.User;
import org.wolfsRealm.ecom_project_2026.payload.AddressDTO;
import org.wolfsRealm.ecom_project_2026.payload.AddressResponse;
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

    @GetMapping("api/addresses")
    public ResponseEntity<AddressResponse> getAddresses(
            @RequestParam(name="pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)Integer pageNumber,
            @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
            @RequestParam(name="sortBy",defaultValue = AppConstants.ADDRESS_SORT_BY,required=false)String sortBy,
            @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_ORDER,required = false)String sortOrder
    ){
        return new ResponseEntity<>(addressService.getAddresses(pageNumber,pageSize,sortBy,sortOrder),HttpStatus.ACCEPTED);

    }

    @GetMapping("api/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId){
        return new ResponseEntity<>(addressService.getAddressById(addressId),HttpStatus.FOUND);
    }

    @GetMapping("api/user/addresses")
    public ResponseEntity<AddressResponse> getAddressesByUser(
            @RequestParam(name="pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)Integer pageNumber,
            @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
            @RequestParam(name="sortBy",defaultValue = AppConstants.ADDRESS_SORT_BY,required=false)String sortBy,
            @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_ORDER,required = false)String sortOrder
    ) {
        User user = authUtil.loggedInUser();
        return new ResponseEntity<>(addressService.getAddressesByUser(user,pageNumber,pageSize,sortBy,sortOrder),HttpStatus.FOUND);
    }

    @PutMapping("api/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId, @RequestBody @Valid  AddressDTO addressDTO){
        return new ResponseEntity<>(addressService.updateAddress(addressId,addressDTO),HttpStatus.ACCEPTED);
    }

    @DeleteMapping("api/addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId){
        return new ResponseEntity<>(addressService.deleteAddress(addressId),HttpStatus.ACCEPTED);
    }


}
