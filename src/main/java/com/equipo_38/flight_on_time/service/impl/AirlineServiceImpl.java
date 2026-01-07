package com.equipo_38.flight_on_time.service.impl;

import com.equipo_38.flight_on_time.exception.AirlineNotFoundException;
import com.equipo_38.flight_on_time.repository.IAirlineRepository;
import com.equipo_38.flight_on_time.service.IAirlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AirlineServiceImpl implements IAirlineService {

    private final IAirlineRepository airlineRepository;

    @Override
    public void validateAirlineCode(String airlineCode) {
        if (!airlineRepository.existsByAirlineCode(airlineCode)){
            throw new AirlineNotFoundException("Airline code "+ airlineCode + " not found");
        }
    }
}
