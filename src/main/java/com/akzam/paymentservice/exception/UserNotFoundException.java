package com.akzam.paymentservice.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApiRequestException {
    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
