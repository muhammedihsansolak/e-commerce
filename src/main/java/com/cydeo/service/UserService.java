package com.cydeo.service;

import com.cydeo.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllCustomers();

    UserDTO updateCustomer(UserDTO userDTO, String email);

    UserDTO create(UserDTO userDTO);

    UserDTO findByEmail(String email);
}
