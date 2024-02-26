package com.cydeo.entity;

import com.cydeo.enums.Role;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {
    private String firstName;
    private String lastName;
    private String userName;
    private String password;

    @Column(unique = true, updatable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Address> addressList;

}
