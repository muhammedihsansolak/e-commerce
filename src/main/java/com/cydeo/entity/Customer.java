package com.cydeo.entity;

import com.cydeo.enums.Role;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Customer extends BaseEntity {
    private String firstName;
    private String lastName;
    private String userName;
    private String password;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "customer")
    private List<Address> addressList;

}
