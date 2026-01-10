package com.equipo_38.flight_on_time.repository;

import com.equipo_38.flight_on_time.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICityRepository extends JpaRepository<City, Long> {

    boolean existsByCityCode(String airportCode);

}