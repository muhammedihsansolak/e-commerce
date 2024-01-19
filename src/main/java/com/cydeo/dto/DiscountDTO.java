package com.cydeo.dto;

import com.cydeo.enums.DiscountType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscountDTO{

    @NotNull
    private String discountCode;

    @NotNull
    private String description;

    @Positive(message = "Discount amount should be positive")
    @NotNull
    private BigDecimal discountAmount;

    @NotNull
    private DiscountType discountType;

    @Positive(message = "Discount minimum amount should be positive")
    private BigDecimal minimumAmount;

    private Long id;
}
