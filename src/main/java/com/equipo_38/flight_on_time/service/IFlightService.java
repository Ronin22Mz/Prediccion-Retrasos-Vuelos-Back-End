package com.equipo_38.flight_on_time.service;

import com.equipo_38.flight_on_time.dto.BatchPredictionDTO;
import com.equipo_38.flight_on_time.dto.FlightRequestDTO;
import com.equipo_38.flight_on_time.dto.PredictionResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface IFlightService {

    PredictionResponseDTO getPrediction(FlightRequestDTO flightRequestDTO);

    BatchPredictionDTO batchPrediction(MultipartFile file);
}
