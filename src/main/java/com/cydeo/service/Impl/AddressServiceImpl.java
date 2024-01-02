package com.cydeo.service.Impl;

import com.cydeo.dto.AddressDTO;
import com.cydeo.entity.Address;
import com.cydeo.entity.Customer;
import com.cydeo.mapper.Mapper;
import com.cydeo.repository.AddressRepository;
import com.cydeo.repository.CustomerRepository;
import com.cydeo.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final Mapper mapperUtil;

    @Override
    public List<AddressDTO> getAll() {
        return addressRepository.findAll()
                .stream().map(address -> mapperUtil.convert(address, new AddressDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public AddressDTO update(AddressDTO addressDTO) {
        addressRepository.save( mapperUtil.convert(addressDTO, new Address()) );
        return addressDTO;
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
        List<Address> addresses = addressRepository.retrieveByCustomerId(customerId);
        return addresses.stream()
                .map(address -> mapperUtil.convert(address, new AddressDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<AddressDTO> findAddressByCustomerIdAndName(Long customerId, String name) {
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        List<Address> list = addressRepository.findAllByCustomerAndName(customer, name);

        return list.stream()
                .map(address -> mapperUtil.convert(address, new AddressDTO()))
                .collect(Collectors.toList());
    }
}







