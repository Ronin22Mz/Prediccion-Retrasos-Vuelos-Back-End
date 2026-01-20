package com.equipo_38.flight_on_time.dto;

public record PredictionDSRequestDTO(

        Integer MONTH,
        Integer DAY_OF_WEEK,
        String ORIGIN,
        String DEST,
        String AIRLINE_CODE,
        String CRS_DEP_TIME,
        String CRS_ARR_TIME,
        Float DISTANCE,
        Float CRS_ELAPSED_TIME

) {

}
