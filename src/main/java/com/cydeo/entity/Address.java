package com.cydeo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Address extends BaseEntity{
    private String name;
    private String zipCode;
    private String street;
    @ManyToOne
    private Customer customer;
}
