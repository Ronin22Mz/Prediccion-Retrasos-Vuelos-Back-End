package com.equipo_38.flight_on_time.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;


@Entity
@Table(name = "prediction_flights")
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class PredictionFlight {

    //inputs
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant creationDate;

    @Column(name = "airline", length = 2, nullable = false)
    @NotBlank
    @Size(min = 2, max = 2)
    private String airline;

    @Column(name = "origin",length = 3, nullable = false)
    @NotBlank
    private String origin;

    @Column(name = "destination", length = 3, nullable = false)
    @NotBlank
    private String destination;

    @Column(name = "departure_date", nullable=false)
    @NotNull
    private LocalDateTime departureDate;

    @DecimalMax("20000")
    @DecimalMin("2.0")
    @NotNull
    private Double distanceKm;

    //outputs
    @Enumerated(EnumType.STRING)
    @NotNull
    private FlightStatus predictionResult;

    @DecimalMax("1.0")
    @DecimalMin("0.0")
    @NotNull
    private Double probability;
}