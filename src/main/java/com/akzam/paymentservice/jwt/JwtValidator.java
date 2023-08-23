package com.akzam.paymentservice.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class JwtValidator {

    private final JwtParser jwtParser;

    @Autowired
    public JwtValidator(JwtParser jwtParser) {
        this.jwtParser = jwtParser;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        Optional<String> extractedUsername = jwtParser.extractUsername(token);
        return extractedUsername.isPresent() &&
                extractedUsername.get().equals(userDetails.getUsername()) &&
                !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Optional<Date> expirationDate = jwtParser.extractExpiration(token);
        return expirationDate.map(date -> date.before(new Date()))
                .orElse(false);
    }
}
