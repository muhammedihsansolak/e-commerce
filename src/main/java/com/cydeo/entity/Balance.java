package com.cydeo.entity;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@NoArgsConstructor
@Table(name = "balances")
public class Balance extends BaseEntity{
    @OneToOne
    private User user;
    private BigDecimal amount;
}
