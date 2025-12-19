package com.equipo_38.flight_on_time.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FlightRequestDTO(

        @NotBlank(message = "{flight.airline.notBlank}")
        @Size(min = 2, max = 2, message = "{flight.airline.size}")
        @Pattern(regexp = "[a-zA-Z0-9]{2}", message = "{flight.airline.pattern}")
        String airline,

        @NotBlank(message = "{flight.airline.notBlank}")
        @Size(min = 3, max = 3, message = "{flight.origin.size}")
        @Pattern(regexp = "[a-zA-Z]{3}", message = "{flight.origin.pattern}")
        String origin,

        @NotBlank(message = "{flight.destination.notBlank}")
        @Size(min = 3, max = 3, message = "{flight.destination.size}")
        @Pattern(regexp = "[a-zA-Z]{3}", message = "{flight.destination.pattern}")
        String destination,

        @NotNull(message = "{flight.departureDate.notNull}")
        @FutureOrPresent(message = "{flight.departureDate.future}")
        LocalDateTime departureDate,

        @NotNull(message = "{flight.distance.notNull}")
        @Positive(message = "{flight.distanceKm.positive}")
        @DecimalMin(value = "2.0", message = "{flight.distance.min}")
        @DecimalMax(value = "20000.0", message = "{flight.distance.max}")
        Double distanceKm
) {
    @AssertTrue(message = "{flight.route.invalid}")
    public boolean isValidRoute() {
        return origin != null && destination != null && !origin.equals(destination);
    }

    @AssertTrue(message = "{flight.departureDate.max}")
    public boolean isValidDepartureDate() {
        return departureDate.isBefore(LocalDateTime.now().plusYears(1));
    }
}
