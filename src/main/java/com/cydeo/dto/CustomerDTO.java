package com.cydeo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class CustomerDTO {

    @JsonIgnore
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
}
