package com.cydeo.controller;

import com.cydeo.dto.response.ResponseWrapper;
import com.cydeo.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add-to-cart")
    public ResponseEntity<ResponseWrapper> addToCart(
            @RequestParam(name = "productCode") String productCode,
            @RequestParam(name = "quantity") Integer quantity)
    {
        boolean result = cartService.addToCart(productCode, quantity);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseWrapper.builder()
                        .success(true)
                        .message("Product successfully added to cart!")
                        .data(result)
                        .statusCode(201)
                        .build()
                );
    }

    //TODO display available discounts
    //TODO select one discount and add to cart
}
