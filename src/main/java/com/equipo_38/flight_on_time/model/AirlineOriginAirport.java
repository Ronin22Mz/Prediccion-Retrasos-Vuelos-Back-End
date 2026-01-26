package com.equipo_38.flight_on_time.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "airline_origin_airport")
@Data
@NoArgsConstructor
public class AirlineOriginAirport {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "airline_origin_airport_seq")
    @SequenceGenerator(
            name = "airline_origin_airport_seq",
            sequenceName = "airline_origin_airport_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "airline_id", nullable = false)
    private Airline airline;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "origin_airport_id", nullable = false)
    private Airport origin;
}