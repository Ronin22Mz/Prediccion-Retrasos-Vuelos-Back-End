package com.equipo_38.flight_on_time.repository.impl;

import com.equipo_38.flight_on_time.repository.IFlightPredictionBatchRepository;
import com.equipo_38.flight_on_time.utils.BatchResponseValidate;
import com.equipo_38.flight_on_time.utils.BatchValidateData;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class IFlightPredictionBatchRepositoryImpl
        implements IFlightPredictionBatchRepository {

    private final JdbcTemplate jdbcTemplate;

    public IFlightPredictionBatchRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<BatchResponseValidate> validateBatchData(
            List<BatchValidateData> batch) {

        if (batch.isEmpty()) {
            return List.of();
        }

        StringBuilder sql = new StringBuilder("""
            SELECT
                batch_data.row_num AS flight_row,
                CASE
                    WHEN NOT EXISTS (
                        SELECT 1 FROM airlines a
                        WHERE a.airline_code = batch_data.airline
                    ) THEN 'AIRLINE_NOT_FOUND'
            
                    WHEN NOT EXISTS (
                        SELECT 1 FROM airports o
                        WHERE o.city_code = batch_data.origin
                    ) THEN 'ORIGIN_NOT_FOUND'
            
                    WHEN NOT EXISTS (
                        SELECT 1 FROM airports d
                        WHERE d.city_code = batch_data.destination
                    ) THEN 'DESTINATION_NOT_FOUND'
            
                    WHEN NOT EXISTS (
                        SELECT 1
                        FROM routes r
                        INNER JOIN airports o ON r.origin_airport_id = o.id
                        INNER JOIN airports d ON r.destination_airport_id = d.id
                        WHERE o.city_code = batch_data.origin
                          AND d.city_code = batch_data.destination
                    ) THEN 'ROUTE_NOT_FOUND'
            
                    ELSE NULL
                END AS error_code
            FROM (
            """);

        for (int i = 0; i < batch.size(); i++) {
            BatchValidateData b = batch.get(i);
            if (i > 0) {
                sql.append("\n    UNION ALL\n");
            }
            sql.append(String.format(
                    "    SELECT %d row_num, '%s' airline, '%s' origin, '%s' destination FROM dual",
                    b.row(),
                    escapeSql(b.airline()),
                    escapeSql(b.origin()),
                    escapeSql(b.destination())
            ));
        }

        sql.append("\n) batch_data\nORDER BY batch_data.row_num");

        return jdbcTemplate.query(
                sql.toString(),
                (rs, idx) -> {
                    String errorCode = rs.getString("error_code");
                    return new BatchResponseValidate(
                            rs.getInt("flight_row"),
                            errorCode == null,
                            errorCode
                    );
                }
        );
    }

    private String escapeSql(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("'", "''");
    }
}