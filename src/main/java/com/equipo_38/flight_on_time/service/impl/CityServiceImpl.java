package com.equipo_38.flight_on_time.service.impl;

import com.equipo_38.flight_on_time.exception.CityNotFoundException;
import com.equipo_38.flight_on_time.repository.ICityRepository;
import com.equipo_38.flight_on_time.service.ICityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements ICityService {

    private final ICityRepository airportRepository;

    @Override
    public void validateCityCode(String airportCode) {
        if (!airportRepository.existsByCityCode(airportCode)){
            throw new CityNotFoundException("City code " + airportCode + " not found");
        }
    }
}
