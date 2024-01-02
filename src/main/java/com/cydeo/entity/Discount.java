package com.cydeo.entity;

import com.cydeo.enums.DiscountType;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Entity
@Data
public class Discount extends BaseEntity{
    private String name;
    private BigDecimal discount;
    @Enumerated(value = EnumType.STRING)
    private DiscountType discountType;
    private BigDecimal minimumAmount;
}
