package com.equipo_38.flight_on_time.mapper;

import com.equipo_38.flight_on_time.dto.FlightRequestDTO;
import com.equipo_38.flight_on_time.dto.PredictionDSRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class FlightDsMapper {

    public PredictionDSRequestDTO toDsRequest(FlightRequestDTO dto) {
        return new PredictionDSRequestDTO(
                dto.airline(),
                dto.origin(),
                dto.destination(),
                dto.departureDate().getHour(),
                dto.departureDate().getDayOfWeek().getValue(),
                dto.distanceKm()
        );
    }
}
