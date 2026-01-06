package com.equipo_38.flight_on_time;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableFeignClients
@SpringBootApplication
public class FlightOnTimeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightOnTimeApplication.class, args);
	}

}
