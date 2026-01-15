package com.equipo_38.flight_on_time.service.impl;

import com.equipo_38.flight_on_time.dto.AirportResponseDTO;
import com.equipo_38.flight_on_time.dto.ResponsePageDTO;
import com.equipo_38.flight_on_time.exception.CityNotFoundException;
import com.equipo_38.flight_on_time.mapper.AirportMapper;
import com.equipo_38.flight_on_time.model.AirlineDestinationAirport;
import com.equipo_38.flight_on_time.model.AirlineOriginAirport;
import com.equipo_38.flight_on_time.repository.IAirlineDestinationAirportRepository;
import com.equipo_38.flight_on_time.repository.IAirlineOriginAirportRepository;
import com.equipo_38.flight_on_time.repository.IAirportRepository;
import com.equipo_38.flight_on_time.service.IAirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements IAirportService {

    private final IAirportRepository airportRepository;
    private final IAirlineOriginAirportRepository airlineOriginAirportRepository;
    private final IAirlineDestinationAirportRepository airlineDestinationAirportRepository;
    private final AirportMapper airportMapper;

    @Override
    public void validateCityCode(String airportCode) {
        if (!airportRepository.existsByCityCode(airportCode)){
            throw new CityNotFoundException("City code " + airportCode + " not found");
        }
    }

    @Override
    @Cacheable(
            value = "airline-origins",
            key = "#idAirline",
            unless = "#result == null || #result.content.isEmpty()"
    )
    public ResponsePageDTO<AirportResponseDTO> getAllOriginsForAirline(Long idAirline) {
        List<AirlineOriginAirport> result = airlineOriginAirportRepository.findAllByAirlineId(idAirline);

        return new ResponsePageDTO<>(result.stream()
                .map(AirlineOriginAirport::getOrigin)
                .map(airportMapper::fromAirportEntity)
                .toList(), result.size());
    }

    @Override
    @Cacheable(
            value = "airline-destinations",
            key = "#idAirline",
            unless = "#result == null || #result.content.isEmpty()"
    )
    public ResponsePageDTO<AirportResponseDTO> getAllDestinationsForAirline(Long idAirline) {
        List<AirlineDestinationAirport> result = airlineDestinationAirportRepository.findAllByAirlineId(idAirline);
        return new ResponsePageDTO<>(result.stream()
                .map(AirlineDestinationAirport::getDestination)
                .map(airportMapper::fromAirportEntity)
                .toList(), result.size());
    }

}
