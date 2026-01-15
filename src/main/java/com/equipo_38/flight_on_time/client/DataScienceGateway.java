package com.equipo_38.flight_on_time.client;

import com.equipo_38.flight_on_time.dto.FlightRequestDTO;
import com.equipo_38.flight_on_time.dto.PredictionDSResponseDTO;
import com.equipo_38.flight_on_time.mapper.FlightDsMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataScienceGateway {
    private final IDataScienceClient dataScienceClient;
    private final FlightDsMapper flightDsMapper;

    @Cacheable(
            cacheNames = "dsPredictionCache",
            key = "#flightRequestDTO.airline + '-' +#flightRequestDTO.origin + '-' + #flightRequestDTO.destination + '-' + #flightRequestDTO.flightDate",
            unless = "#result == null"
    )
    @CircuitBreaker(name = "dsClientPrediction", fallbackMethod = "clientPredictionFallback")
    public PredictionDSResponseDTO getPrediction(FlightRequestDTO flightRequestDTO) {
        return dataScienceClient.getPrediction(flightDsMapper.toDsRequest(flightRequestDTO));
    }

    public PredictionDSResponseDTO clientPredictionFallback(Throwable ex) {
        log.error("DS Client Prediction unavailable", ex);
        return new PredictionDSResponseDTO(1, 0.0);
    }
}
