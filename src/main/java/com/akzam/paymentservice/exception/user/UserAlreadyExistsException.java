package com.akzam.paymentservice.exception.user;

import com.akzam.paymentservice.exception.ApiRequestException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends ApiRequestException {
    public UserAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
