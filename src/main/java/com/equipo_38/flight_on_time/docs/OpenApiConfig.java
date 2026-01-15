package com.equipo_38.flight_on_time.docs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Flight On Time")
                .version("1.0")
                .description("""
                        ### FlightOnTime - Predicción de Retrasos Aéreos
                        
                        Esta API forma parte de la solución integral para la predicción de puntualidad de vuelos comerciales.
                        Utiliza un modelo de **Inteligencia Artificial** entrenado con datos históricos para estimar la probabilidad de retraso en tiempo real.
                        
                        #### Stack Tecnológico
                        * **Core:** Java 21 + Spring Boot 3
                        * **Base de Datos:** PostgreSQL 17 (Dockerizado)
                        * **IA Integration:** Feign Client (comunicación con microservicio Python)
                        * **Documentación:** OpenAPI 3 + Swagger UI
                        * **Infraestructura:** Docker & Docker Compose
                        
                        ####  Funcionalidades Principales
                        1.  **Predicción (/predict):** Endpoint principal que conecta con el modelo de Data Science.
                        2.  **Historial:** Persistencia automática de consultas para auditoría.
                        
                        ---
                        *Desarrollado por el **Equipo 38** para la Hackathon de **No Country** (Simulación Laboral).*
                        """);
    }
}
