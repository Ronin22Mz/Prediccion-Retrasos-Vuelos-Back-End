package com.equipo_38.flight_on_time.docs;

import com.equipo_38.flight_on_time.exception.ApiResponseError;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;

// Esta interfaz define los errores "estándar" que tu API puede devolver.
// Al implementarla en CUALQUIER Controller, Swagger los absorbe automáticamente.
@ApiResponses(value = {
        // --------------------------------------------------------------------------------
        // 400 BAD REQUEST
        // --------------------------------------------------------------------------------
        @ApiResponse(
                responseCode = StatusCode.BAD_REQUEST,
                description = "Datos inválidos (Validación fallida)",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ApiResponseError.class),
                        examples = @ExampleObject(
                                name = "Ejemplo 400",
                                summary = "Error de validación genérico",
                                value = """
                                   {
                                     "message": "Validation Error",
                                     "backendMessage": "Uno o más campos de la solicitud no tienen el formato correcto",
                                     "url": "/api/v1/...",
                                     "method": "POST",
                                     "exceptionDate": "2026/01/07 16:00:00"
                                   }
                                   """
                        )
                )
        ),

        // --------------------------------------------------------------------------------
        // 404 NOT FOUND
        // --------------------------------------------------------------------------------
        @ApiResponse(
                responseCode = StatusCode.NOT_FOUND,
                description = "Recurso no encontrado",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ApiResponseError.class),
                        examples = @ExampleObject(
                                name = "Ejemplo 404",
                                summary = "Recurso no existe",
                                value = """
                                   {
                                     "message": "Resource Not Found",
                                     "backendMessage": "El recurso solicitado no existe en la base de datos",
                                     "url": "/api/v1/...",
                                     "method": "GET",
                                     "exceptionDate": "2026/01/07 16:05:00"
                                   }
                                   """
                        )
                )
        ),

        // --------------------------------------------------------------------------------
        // 429 TOO MANY REQUESTS
        // --------------------------------------------------------------------------------
        @ApiResponse(
                responseCode = StatusCode.TOO_MANY_REQUESTS,
                description = "Cuota de consultas excedida",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ApiResponseError.class),
                        examples = @ExampleObject(
                                name = "Ejemplo 429",
                                summary = "Límite alcanzado",
                                value = """
                                   {
                                     "message": "Rate Limit Exceeded",
                                     "backendMessage": "Ha superado el límite de consultas permitidas por minuto. Intente nuevamente más tarde.",
                                     "url": "/api/v1/...",
                                     "method": "POST",
                                     "exceptionDate": "2026/01/07 16:10:00"
                                   }
                                   """
                        )
                )
        ),

        // --------------------------------------------------------------------------------
        // 500 INTERNAL SERVER ERROR
        // --------------------------------------------------------------------------------
        @ApiResponse(
                responseCode = StatusCode.INTERNAL_SERVER_ERROR,
                description = "Error interno del servidor",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ApiResponseError.class),
                        examples = @ExampleObject(
                                name = "Ejemplo 500",
                                summary = "Error inesperado",
                                value = """
                                   {
                                     "message": "Internal Server Error",
                                     "backendMessage": "Ocurrió un error inesperado al procesar la solicitud (ej. fallo de conexión a servicio externo)",
                                     "url": "/api/v1/...",
                                     "method": "POST",
                                     "exceptionDate": "2026/01/07 16:15:00"
                                   }
                                   """
                        )
                )
        )
})
public interface IStandardApiResponses {
}
