package com.equipo_38.flight_on_time.mapper;

import com.equipo_38.flight_on_time.dto.FlightRequestDTO;
import com.equipo_38.flight_on_time.dto.PredictionDSResponseDTO;
import com.equipo_38.flight_on_time.dto.PredictionRecordDTO;
import com.equipo_38.flight_on_time.dto.PredictionResponseDTO;
import com.equipo_38.flight_on_time.model.FlightStatus;
import com.equipo_38.flight_on_time.model.PredictionFlight;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
        LocalDate fechaDummy = LocalDate.of(1970, 1, 1);
        PredictionFlight entity = new PredictionFlight();
        entity.setAirline(request.airline());
        entity.setOrigin(request.origin());
        entity.setDestination(request.destination());
        entity.setDepartureDate(LocalDateTime.of(request.departureDate(), LocalTime.of(0, 0)));
        entity.setDepartureHour(LocalDateTime.of(fechaDummy, request.departureHour()));
        entity.setArrivedHour(LocalDateTime.of(fechaDummy, request.arrivedHour()));
        entity.setDistanceKm(request.distanceKm());
        // ✅ elapsed time en minutos
        double elapsedMinutes = Duration.between(
                request.departureHour(),
                request.arrivedHour()
        ).toMinutes();

        if (elapsedMinutes < 0) {
            elapsedMinutes += 24 * 60;
        }

        entity.setElapsedTime(elapsedMinutes);

        entity.setPredictionResult(response.forecast());
        entity.setProbability(response.probability());
        return entity;
    }


    public PredictionRecordDTO fromPredictionFlight(PredictionFlight predictionFlight){

        return new PredictionRecordDTO(
                predictionFlight.getAirline(),
                predictionFlight.getOrigin(),
                predictionFlight.getDestination(),
                predictionFlight.getDepartureDate().toLocalDate(),
                predictionFlight.getDepartureHour().toLocalTime(),
                predictionFlight.getArrivedHour().toLocalTime(),
                predictionFlight.getDistanceKm(),
                predictionFlight.getPredictionResult(),
                predictionFlight.getProbability()
        );
    }
}
