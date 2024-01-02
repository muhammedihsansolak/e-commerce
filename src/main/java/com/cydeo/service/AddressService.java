package com.cydeo.service;

import com.cydeo.dto.AddressDTO;

import java.util.List;

public interface AddressService {
    List<AddressDTO> getAll();

    AddressDTO update(AddressDTO addressDTO);

    AddressDTO create(AddressDTO addressDTO);

    List<AddressDTO> findAddressStartsWith(String ch);

    List<AddressDTO> findAddressByCustomerId(Long customerId);

    List<AddressDTO> findAddressByCustomerIdAndName(Long customerId, String name);
}
