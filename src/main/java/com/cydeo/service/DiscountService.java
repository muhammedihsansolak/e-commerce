package com.cydeo.service;

import com.cydeo.dto.DiscountDTO;

import java.util.List;

public interface DiscountService {
    List<DiscountDTO> getAll();

    DiscountDTO update(DiscountDTO discountDTO);

    DiscountDTO createDiscount(DiscountDTO discountDTO);

    DiscountDTO getDiscountByName(String name);
}
