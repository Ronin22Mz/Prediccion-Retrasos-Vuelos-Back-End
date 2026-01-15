package com.equipo_38.flight_on_time.service;

import com.equipo_38.flight_on_time.dto.AirportResponseDTO;
import com.equipo_38.flight_on_time.dto.ResponsePageDTO;

public interface IAirportService {
    void validateCityCode(String airportCode);
    ResponsePageDTO<AirportResponseDTO> getAllOriginsForAirline(Long idAirline);

    ResponsePageDTO<AirportResponseDTO> getAllDestinationsForAirline(Long idAirline);
}