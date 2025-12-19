package com.equipo_38.flight_on_time.dto;

//import com.fasterxml.jackson.annotation.JsonAlias;

public record PredictionDSResponseDTO(

        //@JsonAlias()
        Integer prediction,


        //@JsonAlias()
        Double probability

        //String message
) {
}
