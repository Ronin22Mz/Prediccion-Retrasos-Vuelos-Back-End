package com.equipo_38.flight_on_time.mapper;

import com.equipo_38.flight_on_time.dto.AirlineResponseDTO;
import com.equipo_38.flight_on_time.model.Airline;
import org.springframework.stereotype.Component;

@Component
public class AirlineMapper {
    public AirlineResponseDTO fromAirlineEntity(Airline airline) {
        return new AirlineResponseDTO(airline.getId(), airline.getAirlineCode(), airline.getAirlineName());
    }
}
