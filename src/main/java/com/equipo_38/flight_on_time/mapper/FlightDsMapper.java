package com.equipo_38.flight_on_time.mapper;

import com.equipo_38.flight_on_time.dto.FlightRequestDTO;
import com.equipo_38.flight_on_time.dto.PredictionDSRequestDTO;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

@Component
public class FlightDsMapper {

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm");

    public PredictionDSRequestDTO toDsRequest(FlightRequestDTO dto) {

        float elapsedMinutes = Duration.between(
                dto.departureHour(),
                dto.arrivedHour()
        ).toMinutes();

        // Si cruza medianoche
        if (elapsedMinutes < 0) {
            elapsedMinutes += 24 * 60;
        }

        return new PredictionDSRequestDTO(
                dto.departureDate().getMonthValue(),                 // MONTH (1-12)
                dto.departureDate().getDayOfWeek().getValue() - 1,   // DAY_OF_WEEK (0-6)
                dto.origin(),                                        // ORIGIN
                dto.destination(),                                   // DEST
                dto.airline(),                                       // AIRLINE_CODE
                dto.departureHour().format(TIME_FORMATTER),          // CRS_DEP_TIME "HH:mm"
                dto.arrivedHour().format(TIME_FORMATTER),            // CRS_ARR_TIME "HH:mm"
                dto.distanceKm().floatValue(),                        // DISTANCE
                elapsedMinutes                                       // CRS_ELAPSED_TIME
        );
    }
}

