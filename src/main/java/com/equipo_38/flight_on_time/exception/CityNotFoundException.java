package com.equipo_38.flight_on_time.exception;

public class CityNotFoundException extends RuntimeException {
    public CityNotFoundException(String message) {
        super(message);
    }
}
