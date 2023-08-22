package com.akzam.paymentservice.DTO;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;


@Getter
public class ApiRuntimeExceptionResponse extends ApiExceptionResponseBase {

    private final String errorMessage;

    @Builder
    public ApiRuntimeExceptionResponse(
            int errorCode,
            HttpStatus httpStatus,
            ZonedDateTime timestamp,
            String errorMessage
    ) {
        super(errorCode, httpStatus, timestamp);
        this.errorMessage = errorMessage;
    }
}
