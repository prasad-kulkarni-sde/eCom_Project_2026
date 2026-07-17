package org.wolfsRealm.ecom_project_2026.service;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wolfsRealm.ecom_project_2026.exceptions.ResourceNotFoundException;
import org.wolfsRealm.ecom_project_2026.model.Address;
import org.wolfsRealm.ecom_project_2026.model.User;
import org.wolfsRealm.ecom_project_2026.payload.AddressDTO;
import org.wolfsRealm.ecom_project_2026.repositories.AddressRepository;
import org.wolfsRealm.ecom_project_2026.repositories.UserRepository;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AddressDTO createAddress(@Valid AddressDTO addressDTO, User user) {
        Address address= modelMapper.map(addressDTO,Address.class);
        List<Address> addressList= user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);

        address.setUser(user);
        userRepository.save(user);
        addressRepository.save(address);

        return modelMapper.map(address,AddressDTO.class);






    }
}
