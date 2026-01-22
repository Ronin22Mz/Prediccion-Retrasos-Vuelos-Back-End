package com.equipo_38.flight_on_time.service;

import com.equipo_38.flight_on_time.dto.StatsFilterRequestDTO;
import com.equipo_38.flight_on_time.dto.StatsResponseDTO;

public interface IStatsFlightService {

    StatsResponseDTO getStats(StatsFilterRequestDTO statsFilterRequestDTO);

}
