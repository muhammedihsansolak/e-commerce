package com.cydeo.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Data
public class CartItem extends BaseEntity {
    @ManyToOne
    private Product product;
    private Integer quantity;
    @ManyToOne
    private Cart cart;
}
