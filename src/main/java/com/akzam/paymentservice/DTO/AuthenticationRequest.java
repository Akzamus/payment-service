package com.akzam.paymentservice.DTO;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest (
        @NotBlank(message = "Username cannot be blank")
        String username,
        @NotBlank(message = "Password cannot be blank")
        String password
) { }
