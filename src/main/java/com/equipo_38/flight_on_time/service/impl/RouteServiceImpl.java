package com.equipo_38.flight_on_time.service.impl;

import com.equipo_38.flight_on_time.dto.RouteResponseDTO;
import com.equipo_38.flight_on_time.exception.RouteNotFoundException;
import com.equipo_38.flight_on_time.model.Route;
import com.equipo_38.flight_on_time.repository.IRouteRepository;
import com.equipo_38.flight_on_time.service.IRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements IRouteService {

    private final IRouteRepository routeRepository;

    @Override
    @Cacheable(
            value = "routes",
            key = "#idOrigin + '-' + #idDestination",
            unless = "#result == null"
    )
    public RouteResponseDTO getDistanceFromOriginToDestination(Long idOrigin, Long idDestination) {
        Route route = routeRepository
                .findFirstByOriginIdAndDestinationId(idOrigin, idDestination)
                .orElseThrow(() -> new RouteNotFoundException(
                        "No route found with id " + idOrigin + " and id " + idDestination
                ));
        return new RouteResponseDTO(route.getDistance());
    }
}
