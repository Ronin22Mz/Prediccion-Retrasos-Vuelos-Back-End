package com.equipo_38.flight_on_time.mapper;

import com.equipo_38.flight_on_time.dto.FlightRequestDTO;
import com.equipo_38.flight_on_time.dto.PredictionDSResponseDTO;
import com.equipo_38.flight_on_time.dto.PredictionResponseDTO;
import com.equipo_38.flight_on_time.model.FlightStatus;
import com.equipo_38.flight_on_time.model.PredictionFlight;
import org.springframework.stereotype.Component;

@Component
public class FlightPredictionMapper {

    public PredictionResponseDTO toClientResponse(PredictionDSResponseDTO dsResponse) {
        return new PredictionResponseDTO(
                dsResponse.prediction() == 0
                        ? FlightStatus.ON_TIME
                        : FlightStatus.DELAYED,
                dsResponse.probability()
        );
    }

    public PredictionFlight toEntity(
            FlightRequestDTO request,
            PredictionResponseDTO response
    ) {
        PredictionFlight entity = new PredictionFlight();
        entity.setAirline(request.airline());
        entity.setOrigin(request.origin());
        entity.setDestination(request.destination());
        entity.setDepartureDate(request.departureDate());
        entity.setDistanceKm(request.distanceKm());
        entity.setPredictionResult(response.forecast());
        entity.setProbability(response.probability());
        return entity;
    }
}
