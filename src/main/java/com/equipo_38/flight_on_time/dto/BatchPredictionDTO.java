package com.equipo_38.flight_on_time.dto;

import java.util.List;

public record BatchPredictionDTO(List<PredictionResponseDTO> content,
                                 int total,
                                 int success,
                                 int failed){
    
}
