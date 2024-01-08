package com.cydeo.entity;

import com.cydeo.enums.PaymentMethod;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@NoArgsConstructor
public class Payment extends BaseEntity{
    private BigDecimal paidPrice;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
}
