package com.akzam.paymentservice.controller;

import com.akzam.paymentservice.DTO.auth.AuthenticationRequest;
import com.akzam.paymentservice.DTO.auth.AuthenticationResponse;
import com.akzam.paymentservice.DTO.auth.RegisterRequest;
import com.akzam.paymentservice.service.AuthenticationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @Pattern(regexp = "Bearer .*", message = "Authorization header must start with 'Bearer '")
            @RequestHeader(HttpHeaders.AUTHORIZATION)
            String authHeader
    ) {
        return ResponseEntity.ok(authenticationService.refreshToken(authHeader));
    }
}
