package com.akzam.paymentservice.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends ApiRequestException {
    public UserAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
