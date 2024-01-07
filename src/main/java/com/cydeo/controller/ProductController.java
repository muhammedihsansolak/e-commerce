package com.cydeo.controller;


import com.cydeo.dto.ProductDTO;
import com.cydeo.dto.request.ProductRequest;
import com.cydeo.dto.response.ResponseWrapper;
import com.cydeo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @GetMapping
    public ResponseEntity<ResponseWrapper> fetchAllProducts(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseWrapper("Products successfully retrieved!",
                        productService.getAllProducts(),
                        HttpStatus.OK) );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{productCode}")
    public ResponseEntity<ResponseWrapper> updateProduct(@RequestBody ProductDTO productDTO, @PathVariable("productCode")String productCode){
        return ResponseEntity
                .ok(new ResponseWrapper("Product is updated!",
                        productService.updateProduct(productDTO, productCode),
                        HttpStatus.CREATED ));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ResponseWrapper> createProduct(@RequestBody ProductDTO productDTO){
        return ResponseEntity
                .ok(new ResponseWrapper("Product is created!",
                        productService.createProduct(productDTO),
                        HttpStatus.CREATED ));
    }

    @PostMapping("/categoryandprice")
    public ResponseEntity<ResponseWrapper> findProductByCategoryAndPrice(@RequestBody ProductRequest productRequest){
        return ResponseEntity
                .ok(new ResponseWrapper("Product is created!",
                        productService.findProductByProductRequest(productRequest),
                        HttpStatus.OK ));
    }

    @GetMapping("/{name}")
    public ResponseEntity<ResponseWrapper> fetchProductByName(@PathVariable("name")String name){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseWrapper("Product is successfully retrieved!",
                        productService.getProductByName(name),
                        HttpStatus.OK ));
    }

    @GetMapping("/top3")
    public ResponseEntity<ResponseWrapper> getTop3Product(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseWrapper("Product is successfully retrieved!",
                        productService.getTop3Product(),
                        HttpStatus.ACCEPTED ));
    }

    @GetMapping("/price/{price}")
    public ResponseEntity<ResponseWrapper> countProductByPriceGreaterThan(@PathVariable("price")Integer price){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseWrapper("Product number is successfully retrieved!",
                        productService.countProductByPriceGreaterThan(price),
                        HttpStatus.ACCEPTED ));
    }

    @GetMapping("/price/{price}/quantity/{quantity}")
    public ResponseEntity<ResponseWrapper> getProductByPriceAndQuantity(
            @PathVariable("price")BigDecimal price,
            @PathVariable("quantity") Integer quantity){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseWrapper("Products are successfully retrieved!",
                        productService.getProductsByPriceAndQuantity(price,quantity),
                        HttpStatus.OK));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ResponseWrapper> countProductByPriceGreaterThan(@PathVariable("category")Long categoryId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseWrapper("Products are successfully retrieved!",
                        productService.getProductsByCategory(categoryId),
                        HttpStatus.ACCEPTED ));
    }

}
