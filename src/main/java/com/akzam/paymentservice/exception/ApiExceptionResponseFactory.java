package com.akzam.paymentservice.exception;

import com.akzam.paymentservice.DTO.ApiRuntimeExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class ApiExceptionResponseFactory {

    public ApiRuntimeExceptionResponse createApiRuntimeExceptionResponse(HttpStatus httpStatus, String errorMessage) {
        return ApiRuntimeExceptionResponse.builder()
                .errorCode(httpStatus.value())
                .httpStatus(httpStatus)
                .timestamp(ZonedDateTime.now(ZoneId.of("Z")))
                .errorMessage(errorMessage)
                .build();
    }
}
