package com.akzam.paymentservice.jwt;

import com.akzam.paymentservice.configuration.property.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Component
public class JwtFactory {
    private final JwtProperties jwtProperties;
    private final JwtKeyManager jwtKeyManager;

    @Autowired
    public JwtFactory(JwtProperties jwtProperties, JwtKeyManager jwtKeyManager) {
        this.jwtProperties = jwtProperties;
        this.jwtKeyManager = jwtKeyManager;
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateAccessToken(Collections.emptyMap(), userDetails);
    }

    public String generateAccessToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(
                extraClaims,
                userDetails,
                jwtProperties.getAccessTokenExpiration()
        );
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(
                Collections.emptyMap(),
                userDetails,
                jwtProperties.getRefreshTokenExpiration()
        );
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(jwtKeyManager.getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
