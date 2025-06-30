package com.example.ems.exception;

import com.example.ems.enums.ErrorCode;

import java.time.OffsetDateTime;

public record ErrorDetails(
        OffsetDateTime timestamp,
        String message,
        String path,
        ErrorCode errorCode
) {
}
