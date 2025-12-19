package com.equipo_38.flight_on_time.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FlightRequestDTO(

        @NotBlank
        @Size(min = 2, max = 2)
        @Pattern(regexp = "[a-zA-Z0-9]{2}")
        String airline,

        @NotBlank
        @Size(min = 3, max = 3)
        @Pattern(regexp = "[a-zA-Z]{3}")
        String origin,

        @NotBlank
        @Size(min = 3, max = 3)
        @Pattern(regexp = "[a-zA-Z]{3}")
        String destination,

        @NotNull
        @FutureOrPresent
        LocalDateTime departureDate,

        @NotNull
        @Positive
        @DecimalMin(value = "2.0")
        @DecimalMax(value = "20000.0")
        Double distanceKm
) {
    @AssertTrue(message = "Origin and destination must be different")
    public boolean isValidRoute() {
        return origin != null && destination != null && !origin.equals(destination);
    }

    @AssertTrue(message = "Departure date must not be more than one year in the future")
    public boolean isValidDepartureDate(){
        return departureDate.isBefore(LocalDateTime.now().plusYears(1));
    }
}
