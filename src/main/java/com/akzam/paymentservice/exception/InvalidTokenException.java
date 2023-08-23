package com.akzam.paymentservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidTokenException extends ApiRequestException {
    public InvalidTokenException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
