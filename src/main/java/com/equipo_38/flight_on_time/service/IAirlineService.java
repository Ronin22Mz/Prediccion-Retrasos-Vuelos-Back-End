package com.equipo_38.flight_on_time.service;

import com.equipo_38.flight_on_time.dto.AirlineResponseDTO;
import com.equipo_38.flight_on_time.dto.ResponsePageDTO;

public interface IAirlineService {
    void validateAirlineCode(String airlineCode);
    ResponsePageDTO<AirlineResponseDTO> getAllAirlines();
}
