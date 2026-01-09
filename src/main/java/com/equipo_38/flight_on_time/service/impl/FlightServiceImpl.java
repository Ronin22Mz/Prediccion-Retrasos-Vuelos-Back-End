package com.equipo_38.flight_on_time.service.impl;

import com.equipo_38.flight_on_time.client.DataScienceGateway;
import com.equipo_38.flight_on_time.dto.BatchPredictionDTO;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public BatchPredictionDTO batchPrediction(MultipartFile file) {

        List<PredictionResponseDTO> results = new ArrayList<>();

        int total = 0;
        int success = 0;
        int failed = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {

                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                total++;

                try {
                    FlightRequestDTO request = getFlightRequestDTO(line);

                    PredictionResponseDTO response = getPrediction(request);

                    results.add(response);
                    success++;

                } catch (Exception e) {
                    // Error por fila → no corta el batch
                    failed++;
                }
            }

        } catch (IOException e) {
            // Error grave de archivo → batch inválido
            throw new RuntimeException("Error reading CSV file", e);
        }

        return new BatchPredictionDTO(
                results,
                total,
                success,
                failed
        );
    }

    private FlightRequestDTO getFlightRequestDTO(String line) {
        String[] columns = line.split(",");

        FlightRequestDTO request = new FlightRequestDTO(
                columns[0].trim(),                       // airline
                columns[1].trim(),                       // origin
                columns[2].trim(),                       // destination
                LocalDateTime.now(),      // flightDate
                Double.parseDouble(columns[4].trim())      // distanceKm
        );
        validationData(request);
        return request;
    }

    private void validationData(FlightRequestDTO flightRequestDTO) {
      airlineService.validateAirlineCode(flightRequestDTO.airline());
      cityService.validateCityCode(flightRequestDTO.origin());
      cityService.validateCityCode(flightRequestDTO.destination());
    }
}
