package com.cydeo.dto;

import com.cydeo.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDTO {
    private BigDecimal paidPrice;
    private PaymentMethod paymentMethod;
}
