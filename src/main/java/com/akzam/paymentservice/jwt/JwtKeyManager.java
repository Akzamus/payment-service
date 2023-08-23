package com.akzam.paymentservice.jwt;

import com.akzam.paymentservice.configuration.property.JwtProperties;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtKeyManager {

    private final JwtProperties jwtProperties;

    @Autowired
    public JwtKeyManager(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public Key getSignInKey() {
        String secretKey = jwtProperties.getSecretKey();
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
