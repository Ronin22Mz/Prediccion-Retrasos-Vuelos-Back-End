package com.equipo_38.flight_on_time.repository;

import com.equipo_38.flight_on_time.dto.AirportResponseDTO;
import com.equipo_38.flight_on_time.model.AirlineDestinationAirport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAirlineDestinationAirportRepository extends JpaRepository<AirlineDestinationAirport, Long> {
    List<AirlineDestinationAirport> findAllByAirlineId(Long idAirline);

    @Query("SELECT new com.equipo_38.flight_on_time.dto.AirportResponseDTO(d.id, d.cityCode, d.cityName) " +
            "FROM AirlineDestinationAirport airda JOIN airda.destination d WHERE airda.airline.id = :idAirline")
    List<AirportResponseDTO> findDestinationsDtoByAirlineId(@Param("idAirline") Long idAirline);
}
