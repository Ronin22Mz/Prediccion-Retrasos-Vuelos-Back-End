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
import jakarta.validation.Validator; // <--- IMPORTANTE: Jakarta Validation
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
        int successful = 0;
        int failed = 0;
        int rowNumber = 0;

        // Try-with-resources: Cierra el archivo automáticamente al terminar
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                rowNumber++;

                // 1. Ignorar encabezados (si la línea dice "airline" o "aerolinea")
                if (rowNumber == 1 && (line.toLowerCase().contains("airline") || line.toLowerCase().contains("aerolinea"))) {
                    continue;
                }

                // 2. Ignorar líneas vacías
                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    // 3. Parseo Manual (String -> DTO)
                    String[] data = line.split(",");

                    // Validar columnas mínimas (asumimos 7 columnas según tu ejemplo)
                    if (data.length < 7) {
                        throw new IllegalArgumentException("Columnas insuficientes (se esperaban 7)");
                    }

                    // Limpieza de datos (trim) y conversión
                    String airline = data[0].trim();
                    String origin = data[1].trim();
                    String destination = data[2].trim();
                    // Asumimos formato estándar ISO (YYYY-MM-DD y HH:MM)
                    LocalDate date = LocalDate.parse(data[3].trim());
                    LocalTime depHour = LocalTime.parse(data[4].trim());
                    LocalTime arrHour = LocalTime.parse(data[5].trim());
                    Double distance = Double.parseDouble(data[6].trim());

                    FlightRequestDTO requestDTO = new FlightRequestDTO(
                            airline, origin, destination, date, depHour, arrHour, distance
                    );

                    // 4. Validación Automática (@NotNull, @Pattern, etc.)
                    // Usamos el 'validator' inyectado para revisar las reglas de tu DTO
                    Set<ConstraintViolation<FlightRequestDTO>> violations = validator.validate(requestDTO);

                    if (!violations.isEmpty()) {
                        String errorMsg = violations.stream()
                                .map(ConstraintViolation::getMessage)
                                .collect(Collectors.joining(", "));
                        throw new IllegalArgumentException("Datos inválidos: " + errorMsg);
                    }

                    // 5. Validación de Negocio (Tu método custom en el DTO)
                    if (!requestDTO.isValidRoute()) {
                        throw new IllegalArgumentException("Ruta inválida: Origen y destino son iguales");
                    }

                    // 6. Predicción (Llamamos a tu lógica existente)
                    PredictionResponseDTO prediction = this.getPrediction(requestDTO);

                    // 7. Registro de Éxito
                    items.add(new BatchItemDTO(
                            rowNumber,
                            "SUCCESS",
                            requestDTO,
                            prediction,
                            null
                    ));
                    successful++;

                } catch (Exception e) {
                    // 8. Manejo de Error por Fila (No detiene el proceso)
                    // Captura errores de formato de fecha, validación o lógica
                    items.add(new BatchItemDTO(
                            rowNumber,
                            "FAILED",
                            null,
                            null,
                            e.getMessage() // Guardamos el motivo del fallo
                    ));
                    failed++;
                }
            }

        } catch (IOException e) {
            // Error GRAVE (Problema de lectura de disco/archivo)
            // Aquí usamos tu excepción personalizada
            throw new BatchFileProcessingException("Error fatal al procesar el archivo CSV: " + e.getMessage());
        }

        return new BatchPredictionDTO(successful + failed, successful, failed, items);
    }

    private boolean processLine(String line, List<PredictionResponseDTO> results) {
        try {
            FlightRequestDTO request = getFlightRequestDTO(line);
            PredictionResponseDTO response = getPrediction(request);
            results.add(response);
            return true;
        } catch (Exception e) {
            // Error por fila → no corta el batch
            return false;
        }
    }

    private FlightRequestDTO getFlightRequestDTO(String line) {
        String[] columns = line.split(",");

        FlightRequestDTO request = new FlightRequestDTO(
                columns[0].trim(),                       // airline
                columns[1].trim(),                       // origin
                columns[2].trim(),                       // destination
                LocalDate.parse(columns[3].trim()),     // flightDate
                LocalTime.parse(columns[4].trim()),      //DepartureHour
                LocalTime.parse(columns[5].trim()),      //ArrivedHour
                Double.parseDouble(columns[6].trim())    // distanceKm
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
