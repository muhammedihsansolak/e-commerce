package com.cydeo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO {

    @JsonIgnore
    private Long id;
    private BigDecimal price;
    private Integer quantity;
    private Integer remainingQuantity;
    private String name;

}
