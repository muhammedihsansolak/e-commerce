package com.cydeo.service.Impl;

import com.cydeo.dto.CustomerDTO;
import com.cydeo.entity.Customer;
import com.cydeo.exception.CustomerNotFoundException;
import com.cydeo.mapper.Mapper;
import com.cydeo.repository.CustomerRepository;
import com.cydeo.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
    public CustomerDTO updateCustomer(CustomerDTO customerDTO, String email) {
        Customer foundCustomer = customerRepository.retrieveByCustomerEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with username: " + email));

        Customer customerToSave = mapper.convert(customerDTO, new Customer());
        customerToSave.setId(foundCustomer.getId());
        Customer savedCustomer = customerRepository.save(customerToSave);

        return mapper.convert(savedCustomer, new CustomerDTO());
    }

    @Override
    public CustomerDTO create(CustomerDTO customerDTO) {
        Customer customer = mapper.convert(customerDTO, new Customer());
        Customer savedCustomer = customerRepository.save(customer);

        return mapper.convert(savedCustomer, new CustomerDTO());
    }

    @Override
    public CustomerDTO findByEmail(String email) {
        Customer customer = customerRepository.retrieveByCustomerEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with email: " + email));

        return mapper.convert(customer, new CustomerDTO());
    }
}







