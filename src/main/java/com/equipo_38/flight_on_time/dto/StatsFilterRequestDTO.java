package com.equipo_38.flight_on_time.dto;

public record StatsFilterRequestDTO(
        String airlineCode,
        String originCode,
        String destinationCode,
        Integer initYear,
        Integer endYear,
        Integer initMonth,
        Integer endMonth,
        Integer initDay,
        Integer endDay

) {
}
