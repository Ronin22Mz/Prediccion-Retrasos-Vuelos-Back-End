package com.equipo_38.flight_on_time.dto;

import com.equipo_38.flight_on_time.model.FlightStatus;

public record PredictionResponseDTO(
        FlightStatus forecast,
        Double probability
        ) {
}
