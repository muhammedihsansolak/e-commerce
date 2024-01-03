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
            @RequestParam(name = "customerId") Long customerId,
            @RequestParam(name = "productId") Long productId,
            @RequestParam(name = "quantity") Integer quantity)
    {
        boolean result = cartService.addToCart(customerId, productId, quantity);

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

}
