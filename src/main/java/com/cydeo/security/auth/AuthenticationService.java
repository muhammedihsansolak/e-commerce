package com.cydeo.security.auth;

import com.cydeo.dto.request.AuthenticationRequest;
import com.cydeo.dto.request.RegisterRequest;
import com.cydeo.dto.response.AuthenticationResponse;
import com.cydeo.entity.User;
import com.cydeo.entity.principal.UserPrincipal;
import com.cydeo.enums.Role;
import com.cydeo.exception.CustomerNotFoundException;
import com.cydeo.repository.UserRepository;
import com.cydeo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var customer = new User();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setRole(Role.CUSTOMER);
        repository.save(customer);

        UserPrincipal user = new UserPrincipal(customer);
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );

        var customer = repository.retrieveByCustomerEmail(request.getEmail())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with email: " + request.getEmail()));

        UserPrincipal user = new UserPrincipal(customer);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}