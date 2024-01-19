package com.cydeo.controller;

import com.cydeo.dto.CartItemDTO;
import com.cydeo.dto.response.ResponseWrapper;
import com.cydeo.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart-item")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @GetMapping
    public ResponseEntity<ResponseWrapper> getAllCartItemsForCustomer(){
        List<CartItemDTO> cartItemDTOList = cartItemService.findAll();

        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .message("Cart items successfully retrieved!")
                .data(cartItemDTOList)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }

}
