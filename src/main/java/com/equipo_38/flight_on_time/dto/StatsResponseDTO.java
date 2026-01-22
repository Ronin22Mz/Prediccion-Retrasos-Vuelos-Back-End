package com.equipo_38.flight_on_time.dto;

import java.util.List;

public record StatsResponseDTO(
        Long flightsOnTime,
        Long flightsOnDelayed,
        double percentageOnTime,
        double percentageOnDelayed,
        List<TemporalEvolutionDTO> temporalEvolution
) {
}
