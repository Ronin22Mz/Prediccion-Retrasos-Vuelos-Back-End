package com.equipo_38.flight_on_time.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BatchItemDTO {
    int rowNumber;
    String status;
    FlightRequestDTO input;
    PredictionResponseDTO result;
    String errorMessage;
}

