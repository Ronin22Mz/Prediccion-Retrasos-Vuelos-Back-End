package com.equipo_38.flight_on_time.service.impl;

import com.equipo_38.flight_on_time.client.DataScienceClient;
import com.equipo_38.flight_on_time.dto.FlightRequestDTO;
import com.equipo_38.flight_on_time.dto.PredictionDSRequestDTO;
import com.equipo_38.flight_on_time.dto.PredictionDSResponseDTO;
import com.equipo_38.flight_on_time.dto.PredictionResponseDTO;
import com.equipo_38.flight_on_time.model.FlightStatus;
import com.equipo_38.flight_on_time.model.PredictionFlight;
import com.equipo_38.flight_on_time.repository.FlightPredictionRepository;
import com.equipo_38.flight_on_time.service.IFlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements IFlightService {

    private final FlightPredictionRepository flightPredictionRepository;
    private final DataScienceClient dataScienceClient;

    @Override
    public PredictionResponseDTO getPrediction(FlightRequestDTO flightRequestDTO) {
        PredictionDSResponseDTO predictionDSResponseDTO = dataScienceClient.getPrediction(mapToDsRequest(flightRequestDTO));
        PredictionResponseDTO predictionResponseDTO = mapToClientResponse(predictionDSResponseDTO);
        savePrediction(flightRequestDTO, predictionResponseDTO);
        return predictionResponseDTO;
    }

    private PredictionDSRequestDTO mapToDsRequest(FlightRequestDTO flightRequestDTO) {
        return new PredictionDSRequestDTO(
                flightRequestDTO.airline(),
                flightRequestDTO.origin(),
                flightRequestDTO.destination(),
                flightRequestDTO.departureDate().getHour(),
                flightRequestDTO.departureDate().getDayOfWeek().getValue(),
                flightRequestDTO.distanceKm()
        );
    }

    private PredictionResponseDTO mapToClientResponse(PredictionDSResponseDTO predictionDSResponseDTO) {
        return new PredictionResponseDTO(
                predictionDSResponseDTO.prediction() == 0 ? FlightStatus.ON_TIME : FlightStatus.DELAYED,
                predictionDSResponseDTO.probability()
        );
    }

    private void savePrediction(FlightRequestDTO flightRequestDTO, PredictionResponseDTO predictionResponseDTO) {
        PredictionFlight predictionFlight = new PredictionFlight();
        predictionFlight.setAirline(flightRequestDTO.airline());
        predictionFlight.setOrigin(flightRequestDTO.origin());
        predictionFlight.setDestination(flightRequestDTO.destination());
        predictionFlight.setDepartureDate(flightRequestDTO.departureDate());
        predictionFlight.setDistanceKm(flightRequestDTO.distanceKm());
        predictionFlight.setPredictionResult(predictionResponseDTO.forecast());
        predictionFlight.setProbability(predictionResponseDTO.probability());
        flightPredictionRepository.save(predictionFlight);
    }
}
