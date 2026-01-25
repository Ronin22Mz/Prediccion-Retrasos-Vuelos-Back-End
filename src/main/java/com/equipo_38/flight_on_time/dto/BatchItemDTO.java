package com.equipo_38.flight_on_time.dto;

public record BatchItemDTO(
        int rowNumber,              // Para que sepas qué fila del Excel mirar
        String status,              // "SUCCESS" o "FAILED"
        FlightRequestDTO input,     // (Opcional) Devolvemos los datos que envió para referencia
        PredictionResponseDTO result, // La predicción (Null si falló)
        String errorMessage         // El error (Null si fue éxito)
) {}

