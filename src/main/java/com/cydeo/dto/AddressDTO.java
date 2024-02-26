package com.cydeo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class AddressDTO{

    @JsonIgnore
    private Long id;
    private String name;
    private String zipCode;
    private String street;
    private UserDTO customer;
}
