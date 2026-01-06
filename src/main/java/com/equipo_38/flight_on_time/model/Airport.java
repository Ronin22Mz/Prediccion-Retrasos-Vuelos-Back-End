package com.equipo_38.flight_on_time.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Table(name = "airports") //
@Data // Genera getters, setters, toString, etc. automáticamente
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //
    private Long id;

    @Column(name = "city_code", nullable = false, length = 3)
    @NotBlank(message = "El código de la ciudad no puede estar vacío")
    @Size(min = 3, max = 3, message = "El código debe tener exactamente 3 caracteres (ej: EZE, MIA)")
    @Pattern(regexp = "^[A-Z]+$", message = "El código debe contener solo letras mayúsculas")
    private String cityCode;

    @Column(name = "city_name", nullable = false)
    @NotBlank(message = "El nombre de la ciudad es obligatorio")
    @Size(max = 150, message = "El nombre de la ciudad no puede superar los 150 caracteres")
    private String cityName;
}
