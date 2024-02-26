package com.cydeo.dto;

import com.cydeo.entity.Cart;
import com.cydeo.entity.User;
import com.cydeo.entity.Payment;
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

    @NotNull(message = "Cart is required")
    private Cart cart;

    @Min(value = 1, message = "Paid price can not be lower than 1")
    private BigDecimal paidPrice;

    @Min(value = 1, message = "Total price can not be lower than 1")
    private BigDecimal totalPrice;

    @NotNull(message = "Customer is required")
    private User user;

    @NotNull(message = "Payment is required")
    private Payment paymentId;

}
