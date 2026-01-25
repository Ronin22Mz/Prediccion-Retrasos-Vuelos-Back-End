package com.equipo_38.flight_on_time.service.impl;

import com.equipo_38.flight_on_time.dto.*;
import com.equipo_38.flight_on_time.mapper.FlightPredictionMapper;
import com.equipo_38.flight_on_time.model.FlightStatus;
import com.equipo_38.flight_on_time.model.PredictionFlight;
import com.equipo_38.flight_on_time.repository.IFlightPredictionRepository;
import com.equipo_38.flight_on_time.service.IAirlineService;
import com.equipo_38.flight_on_time.service.IAirportService;
import com.equipo_38.flight_on_time.service.IStatsFlightService;
import com.equipo_38.flight_on_time.utils.PredictionFlightSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsFlightServiceImpl implements IStatsFlightService {

    private final IFlightPredictionRepository flightPredictionRepository;
    private final FlightPredictionMapper flightPredictionMapper;
    private final IAirportService airportService;
    private final IAirlineService airlineService;

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
        List<AirlineResponseDTO> airlines = airlineService.getAllAirlines().content();
        List<AirportResponseDTO> airports = airportService.getAllAirports().content();
        return new ResponsePageDTO<>(flights.map(f-> new PredictionRecordDTO(
                getAirlineName(f.getAirline(),airlines),
                getCityName(f.getOrigin(),airports),
                getCityName(f.getDestination(),airports),
                f.getDepartureDate(),
                f.getDepartureHour(),
                f.getArrivedHour(),
                f.getDistanceKm(),
                f.getPredictionResult(),
                f.getProbability()
        )).getContent(), flights.getTotalElements());
    }

    private String getAirlineName(String airlineCode, List<AirlineResponseDTO> airlines) {


        return airlines.stream()
                .filter(a -> a.airlineCode().equals(airlineCode))
                .findFirst()
                .map(AirlineResponseDTO::airlineName).orElse(null);


    }

    private String getCityName(String cityCode, List<AirportResponseDTO> airports) {


        return airports.stream()
                .filter(a -> a.cityCode().equals(cityCode))
                .findFirst()
                .map(AirportResponseDTO::cityName).orElse(null);


    }

    private double calculatePercentage(long value, long total) {
        double percentage = (double) value * 100 / total;
        return Math.round(percentage * 100.0) / 100.0;
    }

    private List<TemporalEvolutionDTO> buildTemporalEvolutionGroupedByDay(
            List<PredictionFlight> flights) {

        return flights.stream()
                .collect(Collectors.groupingBy(
                        f -> f.getCreationDate()
                                .atZone(ZoneOffset.UTC)
                                .toLocalDate()
                ))

                .entrySet()
                .stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<PredictionFlight> dayFlights = entry.getValue();

                    FlightStatus dominantStatus = dayFlights.stream()
                            .map(PredictionFlight::getPredictionResult)
                            .max(Comparator.comparingInt(Enum::ordinal))
                            .orElse(FlightStatus.ON_TIME);

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
