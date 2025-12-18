package com.equipo_38.flight_on_time.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    private LocalDateTime creationDate;

    private String airline;

    private String origin;

    private String destination;

    private LocalDateTime departureDate;

    private Double distanceKm;

    //outputs
    private FlightStatus predictionResult;

    private Double probability;






}
