package com.equipo_38.flight_on_time.dto;

import com.equipo_38.flight_on_time.model.FlightStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public record PredictionRecordDTO(
        String airline,

        String origin,

        String destination,

        LocalDate departureDate,

        LocalTime departureHour,

        LocalTime arrivedHour,

        Double distanceKm,

        FlightStatus predictionResult,

        Double probability
) {
}
