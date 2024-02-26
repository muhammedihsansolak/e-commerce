package com.cydeo.dto;

import com.cydeo.enums.CartState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class CartDTO {

    @JsonIgnore
    private Long id;
    private UserDTO customer;
    private DiscountDTO discount;
    private CartState cartState;
}
