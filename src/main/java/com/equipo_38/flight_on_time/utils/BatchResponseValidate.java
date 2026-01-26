package com.equipo_38.flight_on_time.utils;

public record BatchResponseValidate(
        int row,
        Boolean isValid,
        String errorCode
) {
}
