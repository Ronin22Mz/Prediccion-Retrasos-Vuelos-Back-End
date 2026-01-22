package com.equipo_38.flight_on_time.utils;

import com.equipo_38.flight_on_time.dto.StatsFilterRequestDTO;
import com.equipo_38.flight_on_time.model.PredictionFlight;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//Filtro aplicado directamente a la base de datos por medio de criteria builder
public class PredictionFlightSpecification {
    private PredictionFlightSpecification() {
    }

    public static Specification<PredictionFlight> withFilters(
            StatsFilterRequestDTO filter
    ) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.airlineCode() != null && !filter.airlineCode().isBlank()) {
                predicates.add(cb.equal(root.get("airline"), filter.airlineCode()));
            }

            if (filter.originCode() != null && !filter.originCode().isBlank()) {
                predicates.add(cb.equal(root.get("origin"), filter.originCode()));
            }

            if (filter.destinationCode() != null && !filter.destinationCode().isBlank()) {
                predicates.add(cb.equal(root.get("destination"), filter.destinationCode()));
            }

            // 🗓️ Construcción de rango de fechas
            if (filter.initYear() > 0 && filter.endYear() > 0) {

                LocalDate startDate = LocalDate.of(
                        filter.initYear(),
                        filter.initMonth() > 0 ? filter.initMonth() : 1,
                        filter.initDay() > 0 ? filter.initDay() : 1
                );

                LocalDate endDate = LocalDate.of(
                        filter.endYear(),
                        filter.endMonth() > 0 ? filter.endMonth() : 12,
                        filter.endDay() > 0 ? filter.endDay() : 31
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
}