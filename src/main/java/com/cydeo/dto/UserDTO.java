package com.cydeo.dto;

import com.cydeo.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class UserDTO {

    @JsonIgnore
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;

    @Email
    private String email;

    @JsonIgnore
    private Role role;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password is required")
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,}", message = "Password must be at least 4 characters long and include at least one digit, one lowercase letter, and one uppercase letter.")
    private String passWord;

    private List<AddressDTO> addressList;
}
