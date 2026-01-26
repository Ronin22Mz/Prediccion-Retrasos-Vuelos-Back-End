package com.equipo_38.flight_on_time.repository;

import com.equipo_38.flight_on_time.utils.BatchResponseValidate;
import com.equipo_38.flight_on_time.utils.BatchValidateData;

import java.util.List;

public interface IFlightPredictionBatchRepository {

    List<BatchResponseValidate> validateBatchData(
            List<BatchValidateData> batchValidateData
    );
}

