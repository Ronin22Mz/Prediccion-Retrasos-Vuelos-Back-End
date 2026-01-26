package com.equipo_38.flight_on_time.utils;

public record BatchValidateData(
        int row,
        String airline,
        String origin,
        String destination
) {
}
