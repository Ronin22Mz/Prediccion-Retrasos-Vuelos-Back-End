package com.equipo_38.flight_on_time.dto;

public record PredictionDSRequestDTO(

        String airline,
        String origin,
        String destination,
        Integer hour,
        Integer dayOfWeek,
        Double distanceKm

) {

}
