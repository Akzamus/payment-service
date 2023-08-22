package com.akzam.paymentservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
public abstract class ApiExceptionResponseBase {
    protected final int errorCode;
    protected final HttpStatus httpStatus;
    protected final ZonedDateTime timestamp;
}
