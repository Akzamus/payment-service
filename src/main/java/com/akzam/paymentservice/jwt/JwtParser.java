package com.akzam.paymentservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Component
public class JwtParser {

    private final JwtKeyManager jwtKeyManager;

    @Autowired
    public JwtParser(JwtKeyManager jwtKeyManager) {
        this.jwtKeyManager = jwtKeyManager;
    }

    public Optional<Date> extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public Optional<String> extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public  <T> Optional<T> extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        T claimValue = claimsResolver.apply(claims);
        return Optional.ofNullable(claimValue);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(jwtKeyManager.getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
