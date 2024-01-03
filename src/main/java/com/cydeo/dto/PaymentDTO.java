package com.cydeo.dto;

import com.cydeo.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDTO {

    @Positive
    private BigDecimal paidPrice;

    private PaymentMethod paymentMethod;
}
