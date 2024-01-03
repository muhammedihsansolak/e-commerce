package com.cydeo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO {

    @JsonIgnore
    private Long id;

    @Positive(message = "Price should be positive")
    private BigDecimal price;

    @Positive(message = "Quantity should be positive")
    private Integer quantity;

    @Positive(message = "Remaining quantity should be positive")
    private Integer remainingQuantity;

    @NotNull(message = "Product name cannot be null")
    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String productCode;

}
