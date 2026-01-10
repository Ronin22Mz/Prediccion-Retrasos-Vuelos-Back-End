package com.equipo_38.flight_on_time.repository;

import com.equipo_38.flight_on_time.model.AirlineOriginAirport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAirlineOriginAirportRepository extends JpaRepository<AirlineOriginAirport, Long> {

}
