package com.cydeo.service.Impl;

import com.cydeo.dto.AddressDTO;
import com.cydeo.entity.Address;
import com.cydeo.entity.User;
import com.cydeo.exception.AddressNotFoundException;
import com.cydeo.exception.CustomerNotFoundException;
import com.cydeo.mapper.Mapper;
import com.cydeo.repository.AddressRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final Mapper mapperUtil;

    @Override
    public List<AddressDTO> getAll() {
        return addressRepository.findAll()
                .stream().map(address -> mapperUtil.convert(address, new AddressDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public AddressDTO update(AddressDTO addressDTO, Long addressId) {
        Address foundAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Address not found with id: " + addressId));

        Address address = mapperUtil.convert(addressDTO, new Address());
        address.setId(foundAddress.getId());
        Address savedAddress = addressRepository.save(address);

        return mapperUtil.convert(savedAddress, new AddressDTO());
    }

    @Override
    public AddressDTO findAddressById(Long addressId) {
        Address foundAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Address not found with id: " + addressId));

        return mapperUtil.convert(foundAddress, new AddressDTO());
    }

    @Override
    public AddressDTO create(AddressDTO addressDTO) {
        Address addressToSave = mapperUtil.convert(addressDTO, new Address());
        Address savedAddress = addressRepository.save(addressToSave);

        return mapperUtil.convert(savedAddress,new AddressDTO());
    }

    @Override
    public List<AddressDTO> findAddressStartsWith(String ch) {
        List<Address> addresses = addressRepository.findAllByStreetStartingWith(ch);
        return addresses.stream()
                .map(address -> mapperUtil.convert(address, new AddressDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<AddressDTO> findAddressByCustomerId(Long customerId) {
        List<Address> addresses = addressRepository.retrieveByUserId(customerId);
        return addresses.stream()
                .map(address -> mapperUtil.convert(address, new AddressDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<AddressDTO> findAddressByCustomerIdAndName(Long customerId, String name) {
        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: "+customerId));

        List<Address> list = addressRepository.findAllByUserAndName(user, name);

        return list.stream()
                .map(address -> mapperUtil.convert(address, new AddressDTO()))
                .collect(Collectors.toList());
    }
}







