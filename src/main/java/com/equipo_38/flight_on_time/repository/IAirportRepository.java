package com.equipo_38.flight_on_time.repository;

import com.equipo_38.flight_on_time.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAirportRepository extends JpaRepository<Airport, Long> {

    boolean existsByCityCode(String airportCode);

}