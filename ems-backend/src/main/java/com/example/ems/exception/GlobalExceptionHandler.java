package com.example.ems.exception;

import com.example.ems.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

import static java.time.OffsetDateTime.now;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Map<Class<? extends Exception>, ErrorCode> ERROR_CODE_MAP = Map.of(
            ResourceAlreadyExistsException.class, ErrorCode.ALREADY_EXISTS,
            ResourceNotFoundException.class, ErrorCode.NOT_FOUND
    );

    private static final Map<Class<? extends Exception>, HttpStatus> STATUS_MAP = Map.of(
            ResourceAlreadyExistsException.class, CONFLICT,
            ResourceNotFoundException.class, NOT_FOUND
    );

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleException(Exception ex, WebRequest request) {
        ErrorCode errorCode = ERROR_CODE_MAP.getOrDefault(ex.getClass(), ErrorCode.INTERNAL_SERVER_ERROR);
        HttpStatus status = STATUS_MAP.getOrDefault(ex.getClass(), INTERNAL_SERVER_ERROR);

        var errorDetails = new ErrorDetails(
                now(),
                ex.getMessage(),
                request.getDescription(false),
                errorCode
        );

        return ResponseEntity.status(status)
                             .body(errorDetails);
    }
}