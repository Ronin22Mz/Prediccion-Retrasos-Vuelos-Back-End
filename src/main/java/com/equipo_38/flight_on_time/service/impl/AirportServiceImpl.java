package com.equipo_38.flight_on_time.service.impl;

import com.equipo_38.flight_on_time.dto.AirportResponseDTO;
import com.equipo_38.flight_on_time.dto.ResponsePageDTO;
import com.equipo_38.flight_on_time.exception.CityNotFoundException;
import com.equipo_38.flight_on_time.mapper.AirportMapper;
import com.equipo_38.flight_on_time.model.Airline;
import com.equipo_38.flight_on_time.model.Airport;
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
        List<AirportResponseDTO> result = airlineOriginAirportRepository.findOriginsDtoByAirlineId(idAirline);
        return new ResponsePageDTO<>(result, (long)result.size());
    }

    @Override
    @Cacheable(
            value = "airline-destinations",
            key = "#idAirline + '-' + #idOrigin",
            unless = "#result == null || #result.content.isEmpty()"
    )
    public ResponsePageDTO<AirportResponseDTO> getAllDestinationsForAirline(Long idAirline, Long idOrigin) {
        List<AirportResponseDTO> result = airportRepository.findDestinationsDTOByAirlineAndOrigin(idAirline,idOrigin);
        return new ResponsePageDTO<>(result, (long)result.size());
    }

    @Override
    @Cacheable(
            cacheNames = "airports",
            unless = "#result.content.isEmpty()"
    )
    public ResponsePageDTO<AirportResponseDTO> getAllAirports() {
        List<Airport> airports = airportRepository.findAll();

        return new ResponsePageDTO<>(airports
                .stream().map(airportMapper::fromAirportEntity).toList(),(long)airports.size());
    }

}
