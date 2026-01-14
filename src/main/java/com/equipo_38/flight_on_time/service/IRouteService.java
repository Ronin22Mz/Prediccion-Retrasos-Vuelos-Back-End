package com.equipo_38.flight_on_time.service;

import com.equipo_38.flight_on_time.dto.RouteResponseDTO;

public interface IRouteService {
    RouteResponseDTO getDistanceFromOriginToDestination(Long idOrigin, Long idDestination);
}
