package com.equipo_38.flight_on_time.controller;


import com.equipo_38.flight_on_time.docs.StatusCode;
import com.equipo_38.flight_on_time.dto.FlightRequestDTO;
import com.equipo_38.flight_on_time.dto.PredictionDSRequestDTO;
import com.equipo_38.flight_on_time.dto.PredictionResponseDTO;
import com.equipo_38.flight_on_time.service.IFlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/flights")
@RequiredArgsConstructor
@Tag(name = "Endpoint de predicción",
description = "Operaciones principales para el analisis de retrasos"
)
public class FlightController {

    private final IFlightService flightService;

    @PostMapping("/predict")
    @Operation(
            summary = "Predecir probabilidad de retraso de vuelo",
            description = """
                    **Core del sistema.** Este endpoint recibe los detalles de un vuelo programado y utiliza un modelo de **Machine Learning** para estimar si sufrirá demoras.
                    
                                **El proceso incluye:**
                                1.  **Validación:** Se verifican formatos de fechas y códigos IATA.
                                2.  **Inferencia:** Se comunica con el microservicio de Data Science (Python).
                                3.  **Persistencia:** La consulta y su resultado se guardan automáticamente en PostgreSQL para auditoría.
                    
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = FlightRequestDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = StatusCode.OK,
                            description = StatusCode.OK_VALUE,
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PredictionResponseDTO.class)
                            )
                    )
            }
    )
    public ResponseEntity<PredictionResponseDTO> predict(@Valid @RequestBody FlightRequestDTO request) {
        PredictionResponseDTO response = flightService.getPrediction(request);
        return ResponseEntity.ok(response);
    }


}
