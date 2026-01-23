package com.equipo_38.flight_on_time.service.impl;

import com.equipo_38.flight_on_time.dto.*;
import com.equipo_38.flight_on_time.mapper.FlightPredictionMapper;
import com.equipo_38.flight_on_time.model.FlightStatus;
import com.equipo_38.flight_on_time.model.PredictionFlight;
import com.equipo_38.flight_on_time.repository.IFlightPredictionRepository;
import com.equipo_38.flight_on_time.service.IStatsFlightService;
import com.equipo_38.flight_on_time.utils.PredictionFlightSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsFlightServiceImpl implements IStatsFlightService {

    private final IFlightPredictionRepository flightPredictionRepository;
    private final FlightPredictionMapper flightPredictionMapper;

    @Override
    public StatsResponseDTO getStats(StatsFilterRequestDTO filter) {

        List<PredictionFlight> flights = flightPredictionRepository
                .findAll(PredictionFlightSpecification.withFilters(filter));

        long totalFlights = flights.size();

        if (totalFlights == 0) {
            return null;
        }

        Map<FlightStatus, Long> statsByStatus = flights.stream()
                .collect(Collectors.groupingBy(
                        PredictionFlight::getPredictionResult,
                        Collectors.counting()
                ));

        long onTime = statsByStatus.getOrDefault(FlightStatus.ON_TIME, 0L);
        long delayed = statsByStatus.getOrDefault(FlightStatus.DELAYED, 0L);

        double onTimePercentage = calculatePercentage(onTime, totalFlights);
        double delayedPercentage = calculatePercentage(delayed, totalFlights);

        List<TemporalEvolutionDTO> temporalEvolution =
                buildTemporalEvolutionGroupedByDay(flights);

        return new StatsResponseDTO(
                onTime,
                delayed,
                onTimePercentage,
                delayedPercentage,
                temporalEvolution
        );
    }

    @Override
    public ResponsePageDTO<PredictionRecordDTO> getPredictionRecords(Pageable pageable) {
        Page<PredictionFlight> flights = flightPredictionRepository.findAll(pageable);
        return new ResponsePageDTO<>(flights.map(flightPredictionMapper::fromPredictionFlight).getContent(),flights.getTotalElements());
    }

    private double calculatePercentage(long value, long total) {
        return (double) value * 100 / total;
    }

    private List<TemporalEvolutionDTO> buildTemporalEvolutionGroupedByDay(
            List<PredictionFlight> flights) {

        return flights.stream()
                .collect(Collectors.groupingBy(
                        f -> f.getCreationDate()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                ))
                .entrySet()
                .stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<PredictionFlight> dayFlights = entry.getValue();

                    // 1️⃣ Estatus dominante del día
                    FlightStatus dominantStatus = dayFlights.stream()
                            .map(PredictionFlight::getPredictionResult)
                            .max(Comparator.comparingInt(Enum::ordinal))
                            .orElse(FlightStatus.ON_TIME);

                    // 2️⃣ Probabilidad promedio del día
                    double avgProbability = dayFlights.stream()
                            .mapToDouble(PredictionFlight::getProbability)
                            .average()
                            .orElse(0);

                    return new TemporalEvolutionDTO(
                            dominantStatus.ordinal(),
                            avgProbability,
                            date
                    );
                })
                .sorted(Comparator.comparing(TemporalEvolutionDTO::date))
                .toList();
    }

}
