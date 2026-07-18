package org.wolfsRealm.ecom_project_2026.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.wolfsRealm.ecom_project_2026.exceptions.ResourceNotFoundException;
import org.wolfsRealm.ecom_project_2026.model.Address;
import org.wolfsRealm.ecom_project_2026.model.Product;
import org.wolfsRealm.ecom_project_2026.model.User;
import org.wolfsRealm.ecom_project_2026.payload.AddressDTO;
import org.wolfsRealm.ecom_project_2026.payload.AddressResponse;
import org.wolfsRealm.ecom_project_2026.payload.ProductDTO;
import org.wolfsRealm.ecom_project_2026.payload.ProductResponse;
import org.wolfsRealm.ecom_project_2026.repositories.AddressRepository;
import org.wolfsRealm.ecom_project_2026.repositories.UserRepository;
import org.wolfsRealm.ecom_project_2026.util.AuthUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthUtil authUtil;

    @Override
    public AddressDTO createAddress(@Valid AddressDTO addressDTO, User user) {
        Address address= modelMapper.map(addressDTO,Address.class);
        List<Address> addressList= user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);

        address.setUser(user);

        addressRepository.save(address);

        return modelMapper.map(address,AddressDTO.class);


    }

    @Override
    public AddressResponse getAddresses(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder= sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Address> addressPage= addressRepository.findAll(pageDetails);


        AddressResponse addressResponse= new AddressResponse();
        List<Address> addresses= addressPage.getContent();
        if (addresses.isEmpty())throw new ResourceNotFoundException("Addresses Not Found!!");


        List<AddressDTO>addressDTOS= addresses.stream().map
                (address -> modelMapper.map(address, AddressDTO.class)).toList();

        addressResponse.setContent(addressDTOS);

        addressResponse.setPageNumber(addressPage.getNumber());
        addressResponse.setTotalPages(addressPage.getTotalPages());
        addressResponse.setTotalElements(addressPage.getTotalElements());
        addressResponse.setPageSize(addressPage.getSize());
        addressResponse.setLast(addressPage.isLast());

        return addressResponse;

    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address= addressRepository.findById(addressId).orElseThrow(()->new ResourceNotFoundException("Address","addressId",addressId));

        return modelMapper.map(address,AddressDTO.class);
    }

    @Override
    public AddressResponse getAddressesByUser(User user, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder= sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Address> addressPage= addressRepository.findByUserId(user.getUserId(),pageDetails);


        AddressResponse addressResponse= new AddressResponse();
        List<Address> addresses= addressPage.getContent();
        if (addresses.isEmpty())throw new ResourceNotFoundException("Addresses Not Found!!");


        List<AddressDTO>addressDTOS= addresses.stream().map
                (address -> modelMapper.map(address, AddressDTO.class)).toList();

        addressResponse.setContent(addressDTOS);

        addressResponse.setPageNumber(addressPage.getNumber());
        addressResponse.setTotalPages(addressPage.getTotalPages());
        addressResponse.setTotalElements(addressPage.getTotalElements());
        addressResponse.setPageSize(addressPage.getSize());
        addressResponse.setLast(addressPage.isLast());

        return addressResponse;
    }

    @Override
    @Transactional
    public AddressDTO updateAddress(Long addressId, @Valid AddressDTO addressDTO) {
        Address address= addressRepository.findById(addressId).orElseThrow(()->new ResourceNotFoundException("Address","addressId",addressId));



        if(addressDTO.getBuildingName()!=null)address.setBuildingName(addressDTO.getBuildingName());
        if(addressDTO.getCity()!=null)address.setCity(addressDTO.getCity());
        if(addressDTO.getStreetName()!=null)address.setStreetName(addressDTO.getStreetName());
        if(addressDTO.getState()!=null)address.setState(addressDTO.getState());
        if(addressDTO.getCountry()!=null)address.setCountry(addressDTO.getCountry());
        if(addressDTO.getPincode()!=null)address.setPincode(addressDTO.getPincode());

        addressRepository.save(address);

        return modelMapper.map(address,AddressDTO.class);


    }

    @Override
    @Transactional
    public String deleteAddress(Long addressId) {
        Address address= addressRepository.findById(addressId).orElseThrow(()->new ResourceNotFoundException("Address","addressId",addressId));

        addressRepository.delete(address);
        return "Address with AddressId: "+addressId+" deleted successfully!!!";
    }
}
