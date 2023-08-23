package com.akzam.paymentservice.configuration.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;


@Data
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "application.security.jwt")
public class JwtProperties {
    private String secretKey;
    private long accessTokenExpiration;
    private long refreshTokenExpiration;
}
