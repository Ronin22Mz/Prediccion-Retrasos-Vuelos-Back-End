package com.equipo_38.flight_on_time.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Estructura estándar de errores de la API")
public record ApiResponseError(
        @Schema(description = "Mensaje descriptivo del error", example = "Validation Data Error")
        String message,
        @Schema(description = "Detalle técnico o validación específica", example = "El origen y el destino deben ser diferentes")
        String backendMessage,
        @Schema(description = "URL solicitada", example = "/api/v1/flights/predict")
        String url,
        @Schema(description = "Método HTTP utilizado", example = "POST")
        String method,
        @Schema(description = "Fecha y hora del error", example = "2026/01/06 19:10:45")
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        LocalDateTime exceptionDate
) {
}
