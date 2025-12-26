package com.equipo_38.flight_on_time.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ApiResponseError(
        String message,
        String backendMessage,
        String url,
        String method,
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        LocalDateTime exceptionDate
) {
}
