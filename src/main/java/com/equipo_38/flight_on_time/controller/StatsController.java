package com.equipo_38.flight_on_time.controller;

import com.equipo_38.flight_on_time.docs.IStandardApiResponses;
import com.equipo_38.flight_on_time.docs.StatusCode;
import com.equipo_38.flight_on_time.dto.RouteResponseDTO;
import com.equipo_38.flight_on_time.dto.StatsFilterRequestDTO;
import com.equipo_38.flight_on_time.dto.StatsResponseDTO;
import com.equipo_38.flight_on_time.service.IStatsFlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v3/flights")
@RequiredArgsConstructor
public class StatsController implements IStandardApiResponses {

    private final IStatsFlightService statsFlightService;

    @GetMapping("/stats")
    @Operation(
            summary = "Retorna estadisticas",
            description = """
                    Este endpoint alimenta los graficos estadisicos establecidos.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = StatusCode.OK,
                            description = StatusCode.OK_VALUE,
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StatsResponseDTO.class)
                            )
                    )
            }
    )
    public ResponseEntity<StatsResponseDTO> getStats(@RequestBody StatsFilterRequestDTO statsFilterRequestDTO) {

        return ResponseEntity.ok(statsFlightService.getStats(statsFilterRequestDTO));

    }

}
