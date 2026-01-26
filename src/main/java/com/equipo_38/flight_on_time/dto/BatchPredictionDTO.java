package com.equipo_38.flight_on_time.dto;

import java.util.List;

public record BatchPredictionDTO(
                                 int total,
                                 Long success,
                                 Long failed,
                                 List<BatchItemDTO> items
) {}
