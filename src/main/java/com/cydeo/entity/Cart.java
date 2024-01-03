package com.cydeo.entity;

import com.cydeo.enums.CartState;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Cart extends BaseEntity{
    @ManyToOne
    private Customer customer;
    @ManyToOne
    private Discount discount;
    @Enumerated(EnumType.STRING)
    private CartState cartState;
}
