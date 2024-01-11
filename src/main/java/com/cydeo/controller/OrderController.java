package com.cydeo.controller;

import com.cydeo.dto.OrderDTO;
import com.cydeo.dto.response.ResponseWrapper;
import com.cydeo.enums.PaymentMethod;
import com.cydeo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ResponseWrapper> getAllOrders(){
        return ResponseEntity.ok(
                new ResponseWrapper(
                        "Orders are successfully retrieved",
                        orderService.getAll(),
                        HttpStatus.ACCEPTED
                )
        );
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getAllCustomerOrders(){
        return ResponseEntity.ok(
                new ResponseWrapper(
                        "Orders are successfully retrieved",
                        orderService.getAllCustomerOrders(),
                        HttpStatus.ACCEPTED
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{orderId}")
    public ResponseEntity<ResponseWrapper> updateOrder(@PathVariable("orderId")Long orderId,@Valid @RequestBody OrderDTO orderDTO){
        return ResponseEntity
                .ok(new ResponseWrapper("Order is updated!",
                        orderService.updateOrder(orderId, orderDTO),
                        HttpStatus.CREATED ));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ResponseWrapper> createOrder(@Valid @RequestBody OrderDTO orderDTO){
        return ResponseEntity.ok(
                new ResponseWrapper(
                        "Order created!",
                        orderService.create(orderDTO),
                        HttpStatus.CREATED
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/paymentMethod/{paymentMethod}")
    public ResponseEntity<ResponseWrapper> getOrdersByPaymentMethod(@PathVariable("paymentMethod")PaymentMethod paymentMethod){
        return ResponseEntity.ok(
                new ResponseWrapper(
                        paymentMethod.name() +" orders retrieved!",
                        orderService.getOrdersByPaymentMethod(paymentMethod),
                        HttpStatus.OK
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/email/{email}")
    public ResponseEntity<ResponseWrapper> getOrdersByEmail(@PathVariable("email")String email){
        return ResponseEntity.ok(
                new ResponseWrapper(
                        "orders retrieved!",
                        orderService.getOrdersByEmail(email),
                        HttpStatus.OK
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper> findById(
            @PathVariable("id")Long id,
            @RequestParam(required = false,name = "currencies") Optional<String> currencies)
    {
        return ResponseEntity.ok(ResponseWrapper.builder()
                .message("Order retrieved!")
                .statusCode(200)
                .success(true)
                .data(orderService.findByIdAndCurrency(id,currencies))
                .build()
        );
    }

    @PostMapping("/place-order")
    public ResponseEntity<ResponseWrapper> placeOrder(
            @RequestParam("paymentMethod")String paymentMethod,
            @RequestParam("discountCode")String discountCode
            )
    {
        BigDecimal paidPrice = orderService.placeOrder(paymentMethod, discountCode);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseWrapper.builder()
                .message("Order placed successfully! Paid price: "+paidPrice)
                .statusCode(201)
                .success(true)
                .data(paidPrice)
                .build()
        );
    }


}
