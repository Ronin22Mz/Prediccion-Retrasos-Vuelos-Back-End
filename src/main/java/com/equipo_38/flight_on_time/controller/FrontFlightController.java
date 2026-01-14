package com.equipo_38.flight_on_time.controller;

import com.equipo_38.flight_on_time.docs.IStandardApiResponses;
import com.equipo_38.flight_on_time.dto.AirlineResponseDTO;
import com.equipo_38.flight_on_time.dto.AirportResponseDTO;
import com.equipo_38.flight_on_time.dto.ResponsePageDTO;
import com.equipo_38.flight_on_time.service.IAirlineService;
import com.equipo_38.flight_on_time.service.IAirportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/flights")
@RequiredArgsConstructor
@Validated
public class FrontFlightController implements IStandardApiResponses {

    private final IAirlineService airlineService;
    private final IAirportService airportService;

    @GetMapping("/airlines")
    @Operation(
            summary = "Devuelve todas las aerolíneas disponibles en la base de datos",
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

    @GetMapping("/origins-by-airline/{idAirline}")
    @Operation(
            summary = "Devuelve todos los orígenes de una aerolínea en especifico",
            description = """
                    "Este endpoint alimenta el Selector Secundario del formulario:
                    Recupera el listado de ciudades de origen disponibles en el sistema para cada aerolínea especificada por su ID.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de ciudades recuperada exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponsePageDTO.class)
                            )
                    )
            }
    )
    public ResponseEntity<ResponsePageDTO<AirportResponseDTO>> getAllOriginsForAirline(@PathVariable("idAirline") @Min(1) Long idAirline) {
        return ResponseEntity.ok(airportService.getAllOriginsForAirline(idAirline));
    }

    @GetMapping("/destinations-by-airline/{idAirline}")
    @Operation(
            summary = "Devuelve todos los destinos de una aerolínea en especifico",
            description = """
                    "Este endpoint alimenta el Selector Secundario del formulario:
                    Recupera el listado de ciudades de destino disponibles en el sistema para cada aerolínea especificada por su ID.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de ciudades recuperada exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponsePageDTO.class)
                            )
                    )
            }
    )
    public ResponseEntity<ResponsePageDTO<AirportResponseDTO>> getAllDestinationsForAirline(@PathVariable("idAirline") @Min(1) Long idAirline) {
        return ResponseEntity.ok(airportService.getAllDestinationsForAirline(idAirline));
    }

}
