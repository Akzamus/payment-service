package com.akzam.paymentservice.DTO.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest (

    @NotBlank(message = "Full name cannot be blank")
    @Size(min = 5, max = 20, message = "full name must be between 5 and 20 characters")
    @Pattern(
            regexp = "^[A-Z][a-z]+\\s[A-Z][a-z]+$",
            message = "Full name must consist of two words with uppercase letters and can include only letters"
    )
    String fullName,

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    String username,

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    String password
) { }
