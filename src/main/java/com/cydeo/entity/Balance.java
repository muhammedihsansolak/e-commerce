package com.cydeo.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Data
public class Balance extends BaseEntity{
    @OneToOne
    private Customer customer;
    private BigDecimal amount;
}
