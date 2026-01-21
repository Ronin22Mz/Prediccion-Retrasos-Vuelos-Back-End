package com.equipo_38.flight_on_time.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseError> handlerGlobalException(HttpServletRequest httpServletRequest, Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseError("Unknown Error", exception.getLocalizedMessage(),
                httpServletRequest.getRequestURL().toString(), httpServletRequest.getMethod(), LocalDateTime.now()));
    }

    @ExceptionHandler(AirlineNotFoundException.class)
    public ResponseEntity<ApiResponseError> handlerAirlineNotFoundException(HttpServletRequest httpServletRequest, AirlineNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseError("Airline not exists for prediction", exception.getLocalizedMessage(),
                        httpServletRequest.getRequestURL().toString(), httpServletRequest.getMethod(), LocalDateTime.now()));
    }

    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<ApiResponseError> handlerCityNotFoundException(HttpServletRequest httpServletRequest, CityNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseError("City not exists for prediction", exception.getLocalizedMessage(),
                        httpServletRequest.getRequestURL().toString(), httpServletRequest.getMethod(), LocalDateTime.now()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseError> handlerMethodArgumentNotValidException(HttpServletRequest httpServletRequest, MethodArgumentNotValidException exception) {
        String validationMessage = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Invalid Data");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseError("Validation Data Error", validationMessage,
                        httpServletRequest.getRequestURL().toString(), httpServletRequest.getMethod(), LocalDateTime.now()));
    }

    @ExceptionHandler(RouteNotFoundException.class)
    public ResponseEntity<ApiResponseError> handlerRouteNotFoundException(HttpServletRequest httpServletRequest, RouteNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseError("Route not found", exception.getLocalizedMessage(),
                        httpServletRequest.getRequestURL().toString(), httpServletRequest.getMethod(), LocalDateTime.now()));
    }
}
