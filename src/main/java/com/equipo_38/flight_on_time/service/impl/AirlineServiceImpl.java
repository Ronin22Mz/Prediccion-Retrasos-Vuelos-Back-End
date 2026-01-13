package com.equipo_38.flight_on_time.service.impl;

import com.equipo_38.flight_on_time.dto.AirlineResponseDTO;
import com.equipo_38.flight_on_time.dto.ResponsePageDTO;
import com.equipo_38.flight_on_time.exception.AirlineNotFoundException;
import com.equipo_38.flight_on_time.mapper.AirlineMapper;
import com.equipo_38.flight_on_time.model.Airline;
import com.equipo_38.flight_on_time.repository.IAirlineRepository;
import com.equipo_38.flight_on_time.service.IAirlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirlineServiceImpl implements IAirlineService {

    private final IAirlineRepository airlineRepository;
    private final AirlineMapper airlineMapper;

    @Override
    public void validateAirlineCode(String airlineCode) {
        if (!airlineRepository.existsByAirlineCode(airlineCode)){
            throw new AirlineNotFoundException("Airline code "+ airlineCode + " not found");
        }
    }

    @Override
    @Cacheable(cacheNames = "airlinesCache", key = "'airlinesKey'", unless = "#result == null")
    public ResponsePageDTO<AirlineResponseDTO> getAllAirlines() {
        List<Airline> airlines = airlineRepository.findAll();
        return new ResponsePageDTO<>(airlines.stream().map(airlineMapper::fromAirlineEntity).toList(), airlines.size());
    }
}
