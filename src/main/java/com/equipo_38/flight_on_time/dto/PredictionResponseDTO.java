package com.equipo_38.flight_on_time.dto;

public record PredictionResponseDTO(
        String forecast,
        Double probability
        ) {
}
