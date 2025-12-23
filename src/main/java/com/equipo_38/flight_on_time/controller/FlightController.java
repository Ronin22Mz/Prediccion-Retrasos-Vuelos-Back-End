package com.equipo_38.flight_on_time.controller;


import com.equipo_38.flight_on_time.dto.FlightRequestDTO;
import com.equipo_38.flight_on_time.dto.PredictionResponseDTO;
import com.equipo_38.flight_on_time.service.IFlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/flights")
@RequiredArgsConstructor
public class FlightController {

    private final IFlightService flightService;

    @PostMapping("/predict")
    public ResponseEntity<PredictionResponseDTO> predict(@Valid @RequestBody FlightRequestDTO request) {
        PredictionResponseDTO response = flightService.getPrediction(request);
        return ResponseEntity.ok(response);
    }


}
