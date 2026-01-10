package com.equipo_38.flight_on_time.service.impl;

import com.equipo_38.flight_on_time.exception.CityNotFoundException;
import com.equipo_38.flight_on_time.repository.IAirportRepository;
import com.equipo_38.flight_on_time.service.IAirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements IAirportService {

    private final IAirportRepository airportRepository;

    @Override
    public void validateCityCode(String airportCode) {
        if (!airportRepository.existsByCityCode(airportCode)){
            throw new CityNotFoundException("City code " + airportCode + " not found");
        }
    }
}
