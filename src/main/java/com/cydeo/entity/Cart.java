package com.cydeo.entity;

import com.cydeo.enums.CartState;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@NoArgsConstructor
public class Cart extends BaseEntity{
    @ManyToOne
    private Customer customer;
    @ManyToOne
    private Discount discount;
    @Enumerated(EnumType.STRING)
    private CartState cartState;
}
