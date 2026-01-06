package com.equipo_38.flight_on_time.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "airlines")
public class Airline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 2)
    @Column(name = "airline_code", length = 2, nullable = false)
    private String airlineCode;
    @NotBlank
    @Size(max = 150)
    @Column(name = "airline_name", length = 150, nullable = false)
    private String airlineName;
}
