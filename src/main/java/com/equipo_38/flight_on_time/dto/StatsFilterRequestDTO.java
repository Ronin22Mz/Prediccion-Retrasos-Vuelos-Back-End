package com.equipo_38.flight_on_time.dto;

public record StatsFilterRequestDTO(
        String airlineCode,
        String originCode,
        String destinationCode,
        int initYear,
        int endYear,
        int initMonth,
        int endMonth,
        int initDay,
        int endDay

) {
}
