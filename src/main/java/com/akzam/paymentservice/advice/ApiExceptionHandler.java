package com.akzam.paymentservice.advice;

import com.akzam.paymentservice.DTO.ApiRuntimeExceptionResponse;
import com.akzam.paymentservice.DTO.ApiValidationExceptionResponse;
import com.akzam.paymentservice.exception.ApiRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiValidationExceptionResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        Map<String, String> errorFields = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(
                        Collectors.toMap(
                                FieldError::getField,
                                FieldError::getDefaultMessage,
                                (existing, replacement) -> replacement
                        )
                );

        return ApiValidationExceptionResponse.builder()
                .errorCode(badRequest.value())
                .httpStatus(badRequest)
                .timestamp(ZonedDateTime.now(ZoneId.of("Z")))
                .errorFields(errorFields)
                .build();
    }

    @ExceptionHandler(ApiRequestException.class)
        public ResponseEntity<ApiRuntimeExceptionResponse> handleApiRequestException(
                ApiRequestException exception
    ) {
        HttpStatus httpStatus = exception.getHttpStatus();
        String errorMessage = exception.getMessage();
        ApiRuntimeExceptionResponse response = createApiRuntimeExceptionResponse(httpStatus, errorMessage);
        return new ResponseEntity<>(response, httpStatus);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ApiRuntimeExceptionResponse> handleMissingRequestHeaderException(
            MissingRequestHeaderException exception
    ) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String errorMessage = "Required header '" + exception.getHeaderName() + "' is missing";
        ApiRuntimeExceptionResponse response = createApiRuntimeExceptionResponse(httpStatus, errorMessage);
        return new ResponseEntity<>(response, httpStatus);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiRuntimeExceptionResponse> handleBadCredentialsException(
            BadCredentialsException exception
    ) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        String errorMessage = exception.getMessage();
        ApiRuntimeExceptionResponse response = createApiRuntimeExceptionResponse(httpStatus, errorMessage);
        return new ResponseEntity<>(response, httpStatus);
    }

    private ApiRuntimeExceptionResponse createApiRuntimeExceptionResponse(HttpStatus httpStatus, String errorMessage) {
        return ApiRuntimeExceptionResponse.builder()
                .errorCode(httpStatus.value())
                .httpStatus(httpStatus)
                .timestamp(ZonedDateTime.now(ZoneId.of("Z")))
                .errorMessage(errorMessage)
                .build();
    }
}
