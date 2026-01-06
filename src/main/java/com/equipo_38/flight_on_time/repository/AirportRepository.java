package com.equipo_38.flight_on_time.repository;

import com.equipo_38.flight_on_time.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {

    // 1. Buscar por código (Útil para validar si existe 'EZE' antes de predecir)
    // Devuelve Optional para que puedas manejar si no existe sin que explote.
    Optional<Airport> findByCityCode(String cityCode);


}