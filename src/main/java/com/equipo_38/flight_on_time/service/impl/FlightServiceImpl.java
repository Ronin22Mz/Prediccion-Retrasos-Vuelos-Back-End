package com.equipo_38.flight_on_time.service.impl;

import com.equipo_38.flight_on_time.client.DataScienceGateway;
import com.equipo_38.flight_on_time.dto.FlightRequestDTO;
import com.equipo_38.flight_on_time.dto.PredictionDSResponseDTO;
import com.equipo_38.flight_on_time.dto.PredictionResponseDTO;
import com.equipo_38.flight_on_time.mapper.FlightPredictionMapper;
import com.equipo_38.flight_on_time.repository.IFlightPredictionRepository;
import com.equipo_38.flight_on_time.service.IAirlineService;
import com.equipo_38.flight_on_time.service.ICityService;
import com.equipo_38.flight_on_time.service.IFlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements IFlightService {

    private final IFlightPredictionRepository flightPredictionRepository;
    private final DataScienceGateway dataScienceGateway;
    private final FlightPredictionMapper flightPredictionMapper;
    private final IAirlineService airlineService;
    private final ICityService cityService;

    @Override
    public PredictionResponseDTO getPrediction(FlightRequestDTO flightRequestDTO) {
        validationData(flightRequestDTO);
        PredictionDSResponseDTO predictionDSResponseDTO = dataScienceGateway.getPrediction(flightRequestDTO);
        PredictionResponseDTO predictionResponseDTO = flightPredictionMapper.toClientResponse(predictionDSResponseDTO);
        flightPredictionRepository.save(flightPredictionMapper.toEntity(flightRequestDTO,predictionResponseDTO));
        return predictionResponseDTO;
    }

    private void validationData(FlightRequestDTO flightRequestDTO) {
      airlineService.validateAirlineCode(flightRequestDTO.airline());
      cityService.validateCityCode(flightRequestDTO.origin());
      cityService.validateCityCode(flightRequestDTO.destination());
    }
}
