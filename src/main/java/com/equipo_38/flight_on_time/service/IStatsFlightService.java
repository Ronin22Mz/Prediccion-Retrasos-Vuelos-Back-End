package com.equipo_38.flight_on_time.service;

import com.equipo_38.flight_on_time.dto.PredictionRecordDTO;
import com.equipo_38.flight_on_time.dto.ResponsePageDTO;
import com.equipo_38.flight_on_time.dto.StatsFilterRequestDTO;
import com.equipo_38.flight_on_time.dto.StatsResponseDTO;
import org.springframework.data.domain.Pageable;

public interface IStatsFlightService {

    StatsResponseDTO getStats(StatsFilterRequestDTO statsFilterRequestDTO);

    ResponsePageDTO<PredictionRecordDTO> getPredictionRecords(Pageable pageable);
}
