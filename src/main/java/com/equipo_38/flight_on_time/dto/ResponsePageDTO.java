package com.equipo_38.flight_on_time.dto;

import java.util.List;

public record ResponsePageDTO<T>(
        List<T> content,
        Long size
) {
}
