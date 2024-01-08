package com.cydeo.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@NoArgsConstructor
@Table(name = "orders")
public class Order extends BaseEntity{
    @OneToOne
    private Cart cart;
    private BigDecimal paidPrice;
    private BigDecimal totalPrice;
    @ManyToOne
    private Customer customer;
    @OneToOne
    private Payment payment;
}
