package com.akzam.paymentservice.DTO;

import lombok.Builder;

@Builder
public record AuthenticationResponse (
        String token
) { }
