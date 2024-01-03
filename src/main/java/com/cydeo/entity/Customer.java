package com.cydeo.entity;

import com.cydeo.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Customer extends BaseEntity {
    private String firstName;
    private String lastName;
    private String userName;
    private String password;

    @Column(unique = true, updatable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role = Role.CUSTOMER;

    @OneToMany(mappedBy = "customer")
    private List<Address> addressList;

}
