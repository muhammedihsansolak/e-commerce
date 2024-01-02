package com.cydeo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDTO {

    @JsonIgnore
    private Long id;

    @NotNull(message = "Cart id is required")
    private Long cartId;

    @Min(value = 1, message = "Paid price can not be lower than 1")
    private BigDecimal paidPrice;

    @Min(value = 1, message = "Total price can not be lower than 1")
    private BigDecimal totalPrice;

    @NotNull(message = "Customer id is required")
    private Long customerId;

    @NotNull(message = "Payment id is required")
    private Long paymentId;

}
