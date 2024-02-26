package com.cydeo.service.Impl;

import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.exception.CustomerNotFoundException;
import com.cydeo.mapper.Mapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Mapper mapper;

    @Override
    public List<UserDTO> getAllCustomers() {
        return userRepository.findAll()
                .stream().map(customer -> mapper.convert(customer, new UserDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateCustomer(UserDTO userDTO, String email) {
        User foundUser = userRepository.retrieveByCustomerEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with username: " + email));

        User userToSave = mapper.convert(userDTO, new User());
        userToSave.setId(foundUser.getId());
        userToSave.setPassword(foundUser.getPassword());
        User savedUser = userRepository.save(userToSave);

        return mapper.convert(savedUser, new UserDTO());
    }

    @Override
    public UserDTO create(UserDTO userDTO) {
        User user = mapper.convert(userDTO, new User());
        User savedUser = userRepository.save(user);

        return mapper.convert(savedUser, new UserDTO());
    }

    @Override
    public UserDTO findByEmail(String email) {
        User user = userRepository.retrieveByCustomerEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with email: " + email));

        return mapper.convert(user, new UserDTO());
    }
}







