package com.equipo_38.flight_on_time.controller;

import com.equipo_38.flight_on_time.docs.IStandardApiResponses;
import com.equipo_38.flight_on_time.docs.StatusCode;
import com.equipo_38.flight_on_time.dto.*;
import com.equipo_38.flight_on_time.service.IAirlineService;
import com.equipo_38.flight_on_time.service.IAirportService;
import com.equipo_38.flight_on_time.service.IRouteService;
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
    private final IRouteService routeService;

    @GetMapping("/airlines")
    @Operation(
            summary = "Devuelve todas las aerolíneas disponibles en la base de datos",
            description = """
                    Este endpoint alimenta el Selector Principal del formulario:
                    Recupera el listado maestro de aerolíneas disponibles en el sistema para iniciar la búsqueda.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = StatusCode.OK,
                            description = StatusCode.OK_VALUE,
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
                    Este endpoint alimenta el Selector Secundario del formulario:
                    Recupera el listado de ciudades de origen disponibles en el sistema para cada aerolínea especificada por su ID.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = StatusCode.OK,
                            description = StatusCode.OK_VALUE,
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

    @GetMapping("/destinations-by-airline/{idAirline}/{idOrigin}")
    @Operation(
            summary = "Devuelve todos los destinos de una aerolínea en especifico",
            description = """
                    Este endpoint alimenta el Selector Terciario del formulario:
                    Recupera el listado de ciudades de destino disponibles en el sistema para cada aerolínea especificada por su ID.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = StatusCode.OK,
                            description = StatusCode.OK_VALUE,
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponsePageDTO.class)
                            )
                    )
            }
    )
    public ResponseEntity<ResponsePageDTO<AirportResponseDTO>> getAllDestinationsForAirline(@PathVariable("idAirline") @Min(1) Long idAirline, @PathVariable("idOrigin") @Min(1) Long idOrigin) {
        return ResponseEntity.ok(airportService.getAllDestinationsForAirline(idAirline,idOrigin));
    }

    @GetMapping("/distance/{idOrigin}/{idDestination}")
    @Operation(
            summary = "Devuelve la distancia en Kms entre el origen y destino",
            description = """
                    Este endpoint alimenta el Selector de distancia del formulario:
                    Recupera la distancia entre la ciudad de origen y destino
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = StatusCode.OK,
                            description = StatusCode.OK_VALUE,
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = RouteResponseDTO.class)
                            )
                    )
            }
    )
    public ResponseEntity<RouteResponseDTO> getDistanceFromOriginToDestination(
            @PathVariable("idOrigin") @Min(1) Long idOrigin,
            @PathVariable("idDestination") @Min(1) Long idDestination
    ){
        return ResponseEntity.ok(routeService.getDistanceFromOriginToDestination(idOrigin,idDestination));
    }

}
