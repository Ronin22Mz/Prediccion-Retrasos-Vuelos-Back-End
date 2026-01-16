package com.equipo_38.flight_on_time.dto;

public record PredictionDSResponseDTO(
        Integer prediction,
        Double probability
) {
}
