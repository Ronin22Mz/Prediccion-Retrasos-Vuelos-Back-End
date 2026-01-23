package com.equipo_38.flight_on_time.utils;

import com.equipo_38.flight_on_time.dto.StatsFilterRequestDTO;
import com.equipo_38.flight_on_time.model.PredictionFlight;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PredictionFlightSpecification {

    private PredictionFlightSpecification() {
    }

    public static Specification<PredictionFlight> withFilters(
            StatsFilterRequestDTO filter
    ) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (hasText(filter.airlineCode())) {
                predicates.add(cb.equal(root.get("airline"), filter.airlineCode()));
            }

            if (hasText(filter.originCode())) {
                predicates.add(cb.equal(root.get("origin"), filter.originCode()));
            }

            if (hasText(filter.destinationCode())) {
                predicates.add(cb.equal(root.get("destination"), filter.destinationCode()));
            }

            if (filter.initYear() != null && filter.endYear() != null) {

                LocalDate startDate = LocalDate.of(
                        filter.initYear(),
                        defaultIfNull(filter.initMonth(), 1),
                        defaultIfNull(filter.initDay(), 1)
                );

                LocalDate endDate = LocalDate.of(
                        filter.endYear(),
                        defaultIfNull(filter.endMonth(), 12),
                        defaultIfNull(filter.endDay(), 31)
                );

                predicates.add(
                        cb.between(
                                root.get("departureDate"),
                                startDate,
                                endDate
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private static int defaultIfNull(Integer value, int defaultValue) {
        return value != null ? value : defaultValue;
    }
}
