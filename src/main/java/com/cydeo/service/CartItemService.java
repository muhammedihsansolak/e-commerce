package com.cydeo.service;

import com.cydeo.dto.CartDTO;
import com.cydeo.dto.CartItemDTO;

import java.util.List;

public interface CartItemService {
    List<CartItemDTO> findAll();
}
