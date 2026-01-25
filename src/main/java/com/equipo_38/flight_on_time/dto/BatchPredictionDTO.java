package com.equipo_38.flight_on_time.dto;

import java.util.List;

public record BatchPredictionDTO(
                                 int total,
                                 int success,
                                 int failed,
                                 List<BatchItemDTO> items // <--- CAMBIO CLAVE: Usamos BatchItemDTO en vez de PredictionResponseDTO
) {}
