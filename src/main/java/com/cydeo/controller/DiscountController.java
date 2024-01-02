package com.cydeo.controller;

import com.cydeo.dto.DiscountDTO;
import com.cydeo.dto.response.ResponseWrapper;
import com.cydeo.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/discount")
public class DiscountController {

    private final DiscountService discountService;

    @GetMapping
    public ResponseEntity<ResponseWrapper> getAllDiscounts() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseWrapper(
                        "Discounts retrieved!",
                        discountService.getAll(),
                        HttpStatus.ACCEPTED
                ));
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper> updateDiscount(@RequestBody DiscountDTO discountDTO){
        return ResponseEntity.ok(new ResponseWrapper(
                "Discount updated!",
                discountService.update(discountDTO),
                HttpStatus.CREATED
        ));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createDiscount(@RequestBody DiscountDTO discountDTO){
        return ResponseEntity.ok(new ResponseWrapper(
                "Discount "+ discountDTO.getName() +" created!",
                discountService.createDiscount(discountDTO),
                HttpStatus.CREATED
        ));
    }

    @GetMapping("/{name}")
    public ResponseEntity<ResponseWrapper> getDiscountByName(@PathVariable("name")String name){
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
        return ResponseEntity.ok(new ResponseWrapper(
                "Discount found! : "+ name,
                discountService.getDiscountByName(encodedName),
                HttpStatus.OK
        ));
    }

}
