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
public class CartItem extends BaseEntity {
    @ManyToOne
    private Product product;
    private Integer quantity;
    @ManyToOne
    private Cart cart;
}
