package com.equipo_38.flight_on_time.controller;

import com.equipo_38.flight_on_time.docs.IStandardApiResponses;
import com.equipo_38.flight_on_time.dto.AirlineResponseDTO;
import com.equipo_38.flight_on_time.dto.ResponsePageDTO;
import com.equipo_38.flight_on_time.service.IAirlineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/flights")
@RequiredArgsConstructor
public class FrontFlightController implements IStandardApiResponses {

    private final IAirlineService airlineService;

    @GetMapping("/airlines")
    @Operation(
            summary = "Devuelve todas las aerolineas disponibles en la base de datos",
            description = """
                    "Este endpoint alimenta el Selector Principal del formulario:
                    Recupera el listado maestro de aerolíneas disponibles en el sistema para iniciar la búsqueda.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de aerolíneas recuperada exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponsePageDTO.class)
                            )
                    )
            }
    )
    public ResponseEntity<ResponsePageDTO<AirlineResponseDTO>> getAllAirlines() {
        return ResponseEntity.ok(airlineService.getAllAirlines());
    }
}
