package com.equipo_38.flight_on_time.controller;


import com.equipo_38.flight_on_time.docs.IStandardApiResponses;
import com.equipo_38.flight_on_time.docs.StatusCode;
import com.equipo_38.flight_on_time.dto.BatchPredictionDTO;
import com.equipo_38.flight_on_time.dto.FlightRequestDTO;
import com.equipo_38.flight_on_time.dto.PredictionResponseDTO;
import com.equipo_38.flight_on_time.service.IFlightService;
import io.swagger.v3.oas.annotations.Operation;
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
import java.util.List;


@RestController
@RequestMapping("/api/v1/flights")
@RequiredArgsConstructor
@Tag(name = "Endpoint de predicción",
description = "Operaciones principales para el analisis de retrasos"
)
public class FlightController implements IStandardApiResponses {

    private final IFlightService flightService;

    @PostMapping("/predict")
    @Operation(
            summary = "Predecir probabilidad de retraso de vuelo",
            description = "Este endpoint es el **Core del sistema**.\n\n" +
                    "Recibe los detalles de un vuelo y utiliza un modelo de **Machine Learning** para estimar demoras.\n\n" +
                    "**El proceso incluye:**\n" +
                    "1. **Validación:** Se verifican formatos de fechas y códigos IATA.\n" +
                    "2. **Inferencia:** Comunicación con el microservicio de Data Science.\n" +
                    "3. **Persistencia:** Guardado automático para auditoría.",
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
    public ResponseEntity<BatchPredictionDTO> predictBatch(@RequestParam("file")MultipartFile file) {

        if(file.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BatchPredictionDTO(Collections.emptyList(),0,0,0));
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(flightService.batchPrediction(file));
        }
    }


}
