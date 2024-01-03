package com.cydeo.controller;

import com.cydeo.dto.OrderDTO;
import com.cydeo.dto.response.ResponseWrapper;
import com.cydeo.enums.PaymentMethod;
import com.cydeo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

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

    @PutMapping("/{orderId}")
    public ResponseEntity<ResponseWrapper> updateOrder(@PathVariable("orderId")Long orderId,@Valid @RequestBody OrderDTO orderDTO){
        return ResponseEntity
                .ok(new ResponseWrapper("Order is updated!",
                        orderService.updateOrder(orderId, orderDTO),
                        HttpStatus.CREATED ));
    }

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

    @PostMapping("/place-order/{paymentMethod}/{customerId}")
    public ResponseEntity<ResponseWrapper> placeOrder(
            @PathVariable("paymentMethod")String paymentMethod,
            @PathVariable("customerId") Long customerId )
    {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseWrapper.builder()
                .message("Order placed successfully!")
                .statusCode(201)
                .success(true)
                .data(orderService.placeOrder(paymentMethod, customerId))
                .build()
        );
    }


}
