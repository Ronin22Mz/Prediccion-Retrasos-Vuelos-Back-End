package com.equipo_38.flight_on_time.service.impl;

import com.equipo_38.flight_on_time.client.DataScienceGateway;
import com.equipo_38.flight_on_time.dto.*;
import com.equipo_38.flight_on_time.exception.BatchFileProcessingException;
import com.equipo_38.flight_on_time.mapper.FlightPredictionMapper;
import com.equipo_38.flight_on_time.repository.IFlightPredictionRepository;
import com.equipo_38.flight_on_time.service.IAirlineService;
import com.equipo_38.flight_on_time.service.IAirportService;
import com.equipo_38.flight_on_time.service.IFlightService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements IFlightService {

    private final IFlightPredictionRepository flightPredictionRepository;
    private final DataScienceGateway dataScienceGateway;
    private final FlightPredictionMapper flightPredictionMapper;
    private final IAirlineService airlineService;
    private final IAirportService cityService;
    private final Validator validator;

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

        List<BatchItemDTO> items = new ArrayList<>();
        int rowNumber = 0;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                rowNumber++;

                if (shouldSkipLine(rowNumber, line)) {
                    continue;
                }

                BatchItemDTO item = safeProcessLine(line, rowNumber);
                items.add(item);
            }

        } catch (IOException e) {
            throw new BatchFileProcessingException(
                    "Error fatal al procesar el archivo CSV");
        }

        long successful = items.stream().filter(i -> "SUCCESS".equals(i.status())).count();
        long failed = items.size() - successful;

        return new BatchPredictionDTO(
                items.size(),
                (int) successful,
                (int) failed,
                items
        );
    }

    private BatchItemDTO safeProcessLine(String line, int rowNumber) {
        try {
            FlightRequestDTO requestDTO = parseAndValidate(line);
            PredictionResponseDTO prediction = getPrediction(requestDTO);

            return new BatchItemDTO(
                    rowNumber,
                    "SUCCESS",
                    requestDTO,
                    prediction,
                    null
            );

        } catch (RuntimeException e) {
            return new BatchItemDTO(
                    rowNumber,
                    "FAILED",
                    null,
                    null,
                    e.getMessage()
            );
        }
    }

    private FlightRequestDTO parseAndValidate(String line) {

        FlightRequestDTO requestDTO = getRequestDTO(line);

        Set<ConstraintViolation<FlightRequestDTO>> violations = validator.validate(requestDTO);
        if (!violations.isEmpty()) {
            String errorMsg = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Datos inválidos: " + errorMsg);
        }

        if (!requestDTO.isValidRoute()) {
            throw new IllegalArgumentException(
                    "Ruta inválida: Origen y destino son iguales");
        }

        return requestDTO;
    }

    private boolean shouldSkipLine(int rowNumber, String line) {
        return rowNumber == 1 || line == null || line.trim().isEmpty();
    }

    private FlightRequestDTO getRequestDTO(String line) {
        String[] data = line.split(",");

        if (data.length < 7) {
            throw new IllegalArgumentException("Columnas insuficientes (se esperaban 7)");
        }

        String airline = data[0].trim();
        String origin = data[1].trim();
        String destination = data[2].trim();
        LocalDate date = LocalDate.parse(data[3].trim());
        LocalTime depHour = LocalTime.parse(data[4].trim());
        LocalTime arrHour = LocalTime.parse(data[5].trim());
        Double distance = Double.parseDouble(data[6].trim());

        return new FlightRequestDTO(
                airline, origin, destination, date, depHour, arrHour, distance
        );
    }

    private void validationData(FlightRequestDTO flightRequestDTO) {
      airlineService.validateAirlineCode(flightRequestDTO.airline());
      cityService.validateCityCode(flightRequestDTO.origin());
      cityService.validateCityCode(flightRequestDTO.destination());
    }
}
