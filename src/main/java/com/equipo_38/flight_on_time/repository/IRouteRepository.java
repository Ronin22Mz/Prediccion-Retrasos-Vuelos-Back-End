package com.equipo_38.flight_on_time.repository;

import com.equipo_38.flight_on_time.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRouteRepository extends JpaRepository<Route,Long> {
    Optional<Route> findFirstByOriginIdAndDestinationId(Long idOrigin, Long idDestination);
}
