package com.cydeo.controller;

import com.cydeo.dto.AddressDTO;
import com.cydeo.dto.response.ResponseWrapper;
import com.cydeo.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ResponseWrapper> getAllAddresses(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseWrapper(
                        "Address's are successfully retrieved",
                        addressService.getAll(),
                        HttpStatus.ACCEPTED
                ));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{addressId}")
    public ResponseEntity<ResponseWrapper> updateAddress(@RequestBody AddressDTO addressDTO, @PathVariable("addressId") Long addressId ){
        return ResponseEntity.ok(new ResponseWrapper(
                "Address updated!",
                addressService.update(addressDTO, addressId),
                HttpStatus.CREATED
        ));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ResponseWrapper> createAddress(@RequestBody AddressDTO addressDTO){
        return ResponseEntity.ok(new ResponseWrapper(
                "Address created!",
                addressService.create(addressDTO),
                HttpStatus.CREATED
        ));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/startsWith/{char}")
    public ResponseEntity<ResponseWrapper> findAddressesStartsWith(@PathVariable("char")String ch ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseWrapper(
                        "Addresses found!",
                        addressService.findAddressStartsWith(ch),
                        HttpStatus.OK
                ));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ResponseWrapper> findAddressByCustomerId(@PathVariable("customerId") Long id ){
        return ResponseEntity
                .ok(new ResponseWrapper(
                        "Addresses found!",
                        addressService.findAddressByCustomerId(id),
                        HttpStatus.OK
                ));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/customer/{customerId}/name/{name}")
    public ResponseEntity<ResponseWrapper> findAddressByCustomerIdAndName(@PathVariable("customerId") Long id,@PathVariable("name") String name){
        return ResponseEntity
                .ok(new ResponseWrapper(
                        "Addresses found!",
                        addressService.findAddressByCustomerIdAndName(id,name),
                        HttpStatus.I_AM_A_TEAPOT
                ));
    }

}
