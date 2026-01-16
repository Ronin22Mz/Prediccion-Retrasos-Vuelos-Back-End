package com.equipo_38.flight_on_time.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "airlines")
@Data
@NoArgsConstructor
public class Airline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 2, max = 2)
    @Column(name = "airline_code", length = 2, nullable = false)
    private String airlineCode;
    @NotBlank
    @Size(max = 150)
    @Column(name = "airline_name", length = 150, nullable = false)
    private String airlineName;

    @OneToMany(mappedBy = "airline")
    @JsonIgnore
    private List<AirlineDestinationAirport> airlineDestinationAirports;

    @OneToMany(mappedBy = "airline")
    @JsonIgnore
    private List<AirlineOriginAirport> airlineOriginAirports;

}
