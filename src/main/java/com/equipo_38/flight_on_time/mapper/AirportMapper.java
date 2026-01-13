package com.equipo_38.flight_on_time.mapper;

import com.equipo_38.flight_on_time.dto.AirportResponseDTO;
import com.equipo_38.flight_on_time.model.Airport;
import org.springframework.stereotype.Component;

@Component
public class AirportMapper {

    public AirportResponseDTO fromAirportEntity(Airport airport) {
        return new AirportResponseDTO(airport.getId(),
                airport.getCityCode(),
                airport.getCityName());
    }

}
