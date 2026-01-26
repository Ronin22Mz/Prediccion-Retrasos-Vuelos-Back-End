package com.equipo_38.flight_on_time.service.impl;

import com.equipo_38.flight_on_time.client.DataScienceGateway;
import com.equipo_38.flight_on_time.dto.*;
import com.equipo_38.flight_on_time.exception.BatchFileProcessingException;
import com.equipo_38.flight_on_time.mapper.FlightPredictionMapper;
import com.equipo_38.flight_on_time.model.PredictionFlight;
import com.equipo_38.flight_on_time.repository.IFlightPredictionRepository;
import com.equipo_38.flight_on_time.service.IAirlineService;
import com.equipo_38.flight_on_time.service.IAirportService;
import com.equipo_38.flight_on_time.service.IFlightService;
import com.equipo_38.flight_on_time.utils.BatchResponseValidate;
import com.equipo_38.flight_on_time.utils.BatchValidateData;
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
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

        List<BatchItemDTO> items;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            AtomicInteger row = new AtomicInteger(2); // 1 = header, data inicia en 2

            items = br.lines()
                    .skip(1)
                    .map(line -> safeProcessLine(line, row.getAndIncrement()))
                    .toList();

        } catch (IOException e) {
            throw new BatchFileProcessingException(
                    "Error fatal al procesar el archivo CSV");
        }

        // Construir batch SOLO de los SUCCESS
        List<BatchValidateData> batchValidateData = items.stream()
                .filter(i -> "SUCCESS".equals(i.getStatus()))
                .map(i -> new BatchValidateData(
                        i.getRowNumber(),
                        i.getInput().airline(),
                        i.getInput().origin(),
                        i.getInput().destination()
                ))
                .toList();

        // Validación en BD (1 query batch)
        if (!batchValidateData.isEmpty()) {

            List<BatchResponseValidate> batchResponseValidates =
                    flightPredictionRepository.validateBatchData(batchValidateData);

            Map<Integer, BatchResponseValidate> validationByRow =
                    batchResponseValidates.stream()
                            .collect(Collectors.toMap(
                                    BatchResponseValidate::row,
                                    Function.identity()
                            ));
            Map<String, String> errorMessages = Map.of(
                    "AIRLINE_NOT_FOUND", "La aerolínea no existe",
                    "ORIGIN_NOT_FOUND", "Aeropuerto de origen inválido",
                    "DESTINATION_NOT_FOUND", "Aeropuerto de destino inválido",
                    "ROUTE_NOT_FOUND", "La ruta no existe"
            );

            items.forEach(item -> {
                if (!"SUCCESS".equals(item.getStatus())) {
                    return;
                }

                BatchResponseValidate validation =
                        validationByRow.get(item.getRowNumber());

                if (validation != null && !validation.isValid()) {
                    item.setStatus("FAILED");
                    String message = errorMessages.getOrDefault(
                            validation.errorCode(),
                            "Error de validación en base de datos"
                    );

                    item.setErrorMessage(message);
                }
            });
            items.forEach(item -> {
                if (!"SUCCESS".equals(item.getStatus())) {
                    return;
                }

                try {
                    PredictionResponseDTO result =
                            getPredictionResponse(item.getInput());

                    item.setResult(result);

                } catch (Exception ex) {
                    item.setStatus("FAILED");
                    item.setErrorMessage("Error Data Science Client");
                }
            });
            List<PredictionFlight> predictionFlights =
                    items.stream()
                            .filter(i -> "SUCCESS".equals(i.getStatus()))
                            .map(item ->
                                    flightPredictionMapper.toEntity(
                                            item.getInput(),
                                            item.getResult()
                                    )
                            )
                            .toList();
            flightPredictionRepository.saveAll(predictionFlights);

        }

        long success = items.stream()
                .filter(i -> "SUCCESS".equals(i.getStatus()))
                .count();

        long failed = items.stream()
                .filter(i -> "FAILED".equals(i.getStatus()))
                .count();

        return new BatchPredictionDTO(
                items.size(),
                success,
                failed,
                items
        );
    }


    private PredictionResponseDTO getPredictionResponse(FlightRequestDTO flightRequestDTO){
        PredictionDSResponseDTO predictionDSResponseDTO = dataScienceGateway.getPrediction(flightRequestDTO);
        return flightPredictionMapper.toClientResponse(predictionDSResponseDTO);
    }

    private BatchItemDTO safeProcessLine(String line, int rowNumber) {
        try {
            if (line.isBlank()){
                throw new IllegalArgumentException("line is blank");
            }
            FlightRequestDTO requestDTO = parseAndValidate(line);

            return BatchItemDTO.builder()
                    .rowNumber(rowNumber)
                    .status("SUCCESS")
                    .input(requestDTO)
                    .build();

        } catch (RuntimeException e) {
            return BatchItemDTO.builder()
                    .rowNumber(rowNumber)
                    .status("FAILED")
                    .errorMessage(e.getLocalizedMessage())
                    .build();
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