package com.cydeo.entity;

import com.cydeo.enums.DiscountType;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@NoArgsConstructor
public class Discount extends BaseEntity{

    @Column(unique = true, updatable = false)
    private String discountCode;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal discountAmount;

    @Enumerated(value = EnumType.STRING)
    private DiscountType discountType;

    private BigDecimal minimumAmount;
}
