package com.equipo_38.flight_on_time.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "airline_destination_airport")
@Data
@NoArgsConstructor
public class AirlineDestinationAirport {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "airline_destination_airport_seq")
    @SequenceGenerator(
            name = "airline_destination_airport_seq",
            sequenceName = "airline_destination_airport_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "airline_id", nullable = false)
    private Airline airline;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destination_airport_id", nullable = false)
    private Airport destination;
}