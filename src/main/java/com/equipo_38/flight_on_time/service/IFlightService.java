package com.equipo_38.flight_on_time.service;

import com.equipo_38.flight_on_time.dto.FlightRequestDTO;
import com.equipo_38.flight_on_time.dto.PredictionResponseDTO;

public interface FlightService {

    PredictionResponseDTO getPrediction(FlightRequestDTO flightRequestDTO);
}
