package com.equipo_38.flight_on_time.controller;


import com.equipo_38.flight_on_time.docs.IStandardApiResponses;
import com.equipo_38.flight_on_time.docs.StatusCode;
import com.equipo_38.flight_on_time.dto.BatchPredictionDTO;
import com.equipo_38.flight_on_time.dto.FlightRequestDTO;
import com.equipo_38.flight_on_time.dto.PredictionResponseDTO;
import com.equipo_38.flight_on_time.service.IFlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;

@RestController
@RequestMapping("/api/v1/flights")
@RequiredArgsConstructor
@Tag(name = "Endpoint de predicción",
description = "Operaciones principales para el análisis de retrasos"
)
public class FlightController implements IStandardApiResponses {

    private final IFlightService flightService;

    @PostMapping("/predict")
    @Operation(
            summary = "Predecir probabilidad de retraso de vuelo",
            description = """
                    Este endpoint es el Core del sistema.
                    Recibe los detalles de un vuelo y utiliza un modelo de Machine Learning para estimar demoras.
                    El proceso incluye:
                        1. Validación: Se verifican formatos de fechas y códigos IATA.
                        2. Inferencia: Comunicación con el microservicio de Data Science
                        3. Persistencia: Guardado automático para auditoría.
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


    @PostMapping(value = "/batch-csv",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Predicción batch de retrasos a partir de archivo CSV",
            description = """
                Permite realizar múltiples predicciones de retraso de vuelos en una sola operación.
                El endpoint recibe un archivo CSV con múltiples registros de vuelos y ejecuta
                el proceso de inferencia de forma batch.
               
                ### Flujo del proceso:
                    1. Validación estructural del archivo CSV.
                    2. Procesamiento batch de predicciones.
                    3. Persistencia y agregación de resultados.
               
                El CSV debe contener una fila por vuelo con las columnas esperadas
                por el modelo de Machine Learning.
               
                ### **Ejemplo (`.csv`):**
                    airline,origin,destiny,departureDate,distanceKm
                    AZ,GIG,GRU,2025-11-10T14:30:00,350
                    AZ,GRU,GIG,2025-11-11T09:15:00,350
                    LA,SCL,LIM,2025-11-12T06:45:00,2450
                    AV,BOG,MEX,2025-11-13T13:20:00,3150
                    IB,MAD,BCN,2025-11-14T08:00:00,505
                """
            ,
            responses = {
                    @ApiResponse(
                            responseCode = StatusCode.OK,
                            description = "Predicción batch procesada correctamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BatchPredictionDTO.class)
                            )
                    )
            }
    )
    public ResponseEntity<BatchPredictionDTO> predictBatch(
            @Parameter(
                    description = "Archivo CSV con múltiples vuelos a predecir",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @RequestParam("file") MultipartFile file
    ) {

        if(file.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BatchPredictionDTO(Collections.emptyList(),0,0,0));
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(flightService.batchPrediction(file));
        }
    }


}
