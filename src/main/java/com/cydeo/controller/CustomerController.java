package com.cydeo.controller;

import com.cydeo.dto.CustomerDTO;
import com.cydeo.dto.response.ResponseWrapper;
import com.cydeo.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ResponseWrapper> getCustomers(){
        return ResponseEntity.ok(new ResponseWrapper(
                true,
                "Customers are successfully retrieved!",
                HttpStatus.ACCEPTED.value(),
                customerService.getAllCustomers()
        ));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{email}")
    public ResponseEntity<ResponseWrapper> updateCustomer(@RequestBody CustomerDTO customerDTO, @PathVariable("email") String email){
        return ResponseEntity.ok(new ResponseWrapper(
                "Customer updated!",
                customerService.updateCustomer(customerDTO, email),
                HttpStatus.CREATED
        ));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ResponseWrapper> createCustomer(@RequestBody CustomerDTO customerDTO){
        return ResponseEntity.ok(new ResponseWrapper(
                "Customer created!!",
                customerService.create(customerDTO),
                HttpStatus.I_AM_A_TEAPOT
        ));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{email}")
    public ResponseEntity<ResponseWrapper> getCustomerByEmail(@PathVariable("email")String email){
        return ResponseEntity.ok(new ResponseWrapper(
                "Customer found!",
                customerService.findByEmail(email),
                HttpStatus.OK
        ));
    }

}





