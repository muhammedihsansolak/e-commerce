package com.cydeo.service.Impl;

import com.cydeo.dto.CustomerDTO;
import com.cydeo.entity.Customer;
import com.cydeo.mapper.Mapper;
import com.cydeo.repository.CustomerRepository;
import com.cydeo.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final Mapper mapper;

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream().map(customer -> mapper.convert(customer, new CustomerDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        customerRepository.save(mapper.convert(customerDTO, new Customer()));
        return customerDTO;
    }

    @Override
    public CustomerDTO create(CustomerDTO customerDTO) {
        customerRepository.save(mapper.convert(customerDTO, new Customer()));
        return customerDTO;
    }

    @Override
    public CustomerDTO findByEmail(String email) {
        Customer customer = customerRepository.retrieveByCustomerEmail(email);
        return mapper.convert(customer, new CustomerDTO());
    }
}







