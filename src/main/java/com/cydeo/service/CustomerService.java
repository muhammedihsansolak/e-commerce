package com.cydeo.service;

import com.cydeo.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {
    List<CustomerDTO> getAllCustomers();

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    CustomerDTO create(CustomerDTO customerDTO);

    CustomerDTO findByEmail(String email);
}
