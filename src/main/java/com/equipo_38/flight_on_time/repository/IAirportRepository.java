package com.equipo_38.flight_on_time.repository;

import com.equipo_38.flight_on_time.dto.AirportResponseDTO;
import com.equipo_38.flight_on_time.model.Airport;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAirportRepository extends JpaRepository<Airport, Long> {

    boolean existsByCityCode(String airportCode);

    @Query("""
           SELECT new com.equipo_38.flight_on_time.dto.AirportResponseDTO(a.id, a.cityCode, a.cityName)
           FROM Airport a
           JOIN AirlineDestinationAirport ada ON a.id = ada.destination.id
           JOIN Route r ON r.destination.id = a.id
           WHERE ada.airline.id = :airlineId
             AND r.origin.id = :originId
           """)
    List<AirportResponseDTO> findDestinationsDTOByAirlineAndOrigin(@Param("airlineId") Long airlineId,
                                                                   @Param("originId") Long originId);

}