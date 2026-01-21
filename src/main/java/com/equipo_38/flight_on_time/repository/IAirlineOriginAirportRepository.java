package com.equipo_38.flight_on_time.repository;

import com.equipo_38.flight_on_time.dto.AirportResponseDTO;
import com.equipo_38.flight_on_time.model.AirlineOriginAirport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAirlineOriginAirportRepository extends JpaRepository<AirlineOriginAirport, Long> {

    List<AirlineOriginAirport> findAllByAirlineId(Long idAirline);

    @Query("SELECT new com.equipo_38.flight_on_time.dto.AirportResponseDTO(o.id, o.cityCode, o.cityName) " +
            "FROM AirlineOriginAirport aoa JOIN aoa.origin o WHERE aoa.airline.id = :idAirline")
    List<AirportResponseDTO> findOriginsDtoByAirlineId(@Param("idAirline") Long idAirline);

}
