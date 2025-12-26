package com.equipo_38.flight_on_time.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseError> handlerGlobalException(HttpServletRequest httpServletRequest, Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseError("Unknown Error", exception.getLocalizedMessage(),
                httpServletRequest.getRequestURL().toString(), httpServletRequest.getMethod(), LocalDateTime.now()));
    }
}
