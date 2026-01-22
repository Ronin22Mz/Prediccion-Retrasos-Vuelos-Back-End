package com.equipo_38.flight_on_time.dto;

import java.time.LocalDate;

public record TemporalEvolutionDTO(
        Integer predictionResult,
        double probability,
        LocalDate date
) {
}
