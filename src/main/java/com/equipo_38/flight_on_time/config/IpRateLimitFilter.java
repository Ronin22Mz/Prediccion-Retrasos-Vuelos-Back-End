package com.equipo_38.flight_on_time.config;

import com.equipo_38.flight_on_time.exception.ApiResponseError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
@Order(1)
@RequiredArgsConstructor
@Slf4j
public class IpRateLimitFilter extends OncePerRequestFilter {

    @Value("${rate-limit.ip.request-per-minute}")
    private int requestPerMinute;


    private final Cache<String, Bucket> buckets = Caffeine.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .maximumSize(1_000).build();

    // Inyectamos esto para convertir tu objeto Java a JSON texto
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String clientIp = getClientIp(request);
        Bucket bucket = buckets.get(clientIp, this::createNewBucket);

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            log.warn("Rate limit exceeded for IP {}", clientIp);
            responseWithDataError(request, response);
        }
    }

    private void responseWithDataError(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. Preparamos la respuesta HTTP
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // "application/json"
        response.setHeader("X-Rate-Limit-Limit", String.valueOf(requestPerMinute));
        response.setHeader("X-Rate-Limit-Remaining", "0");

        // 2. Creamos TU objeto de error estándar
        ApiResponseError error = new ApiResponseError(
                "Rate Limit Exceeded",
                "Has superado el límite de " + requestPerMinute + " peticiones por minuto.",
                request.getRequestURI(),
                request.getMethod(),
                LocalDateTime.now()
        );

        // 3. Lo convertimos a JSON y lo enviamos
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }

    private Bucket createNewBucket(String ip) {
        return Bucket.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(requestPerMinute)
                        .refillIntervally(requestPerMinute, Duration.ofMinutes(1))
                        .build())
                .build();
    }

    private String getClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}