package com.cydeo.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.math.BigDecimal;

@Entity
@Data
public class Balance extends BaseEntity{
    @OneToOne
    private Customer customer;
    private BigDecimal amount;
}
