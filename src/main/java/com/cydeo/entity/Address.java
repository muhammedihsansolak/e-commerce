package com.cydeo.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@NoArgsConstructor
@Table(name = "addresses")
public class Address extends BaseEntity{
    private String name;
    private String zipCode;
    private String street;
    @ManyToOne
    private User user;
}
