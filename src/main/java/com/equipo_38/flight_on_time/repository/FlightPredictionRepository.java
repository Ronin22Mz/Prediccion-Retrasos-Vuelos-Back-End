package com.equipo_38.flight_on_time.repository;

import com.equipo_38.flight_on_time.model.PredictionFlight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightPredictionRepository  extends JpaRepository<Long, PredictionFlight> {


}
