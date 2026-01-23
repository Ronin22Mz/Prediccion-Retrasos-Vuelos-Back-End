package com.equipo_38.flight_on_time.config;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Value("${url.frontend.dev}")
    private String urlFrontendDev;
    @Value("${url.frontend.web}")
    private String urlFrontendWeb;
    @Value("${url.backend.dev}")
    private String urlBackendDev;
    @Value("${url.backend.server}")
    private String urlBackendServer;

    public WebMvcConfigurer corsConfigurer() {

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                WebMvcConfigurer.super.addCorsMappings(registry);
                registry.addMapping("/**")
                        .allowedOrigins(urlFrontendDev, urlFrontendWeb, urlBackendDev, urlBackendServer)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(false);
            }
        };
    }
}
