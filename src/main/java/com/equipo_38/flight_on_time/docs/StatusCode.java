package com.equipo_38.flight_on_time.docs;

public class StatusCode {


    private StatusCode() {
    }

    // 2xx - Éxito
    public static final String OK = "200";
    public static final String OK_VALUE = "OK";

    public static final String CREATED = "201";
    public static final String CREATED_VALUE = "Created";

    public static final String NO_CONTENT = "204";
    public static final String NO_CONTENT_VALUE = "No Content";

    // 4xx - Errores del cliente
    public static final String BAD_REQUEST = "400";
    public static final String BAD_REQUEST_VALUE = "Bad Request";

    public static final String UNAUTHORIZED = "401";
    public static final String UNAUTHORIZED_VALUE = "Unauthorized";

    public static final String FORBIDDEN = "403";
    public static final String FORBIDDEN_VALUE = "Forbidden";

    public static final String NOT_FOUND = "404";
    public static final String NOT_FOUND_VALUE = "Not Found";

    public static final String CONFLICT = "409";
    public static final String CONFLICT_VALUE = "Conflict";

    public static final String TOO_MANY_REQUESTS = "429";
    public static final String TOO_MANY_REQUESTS_VALUE = "Too Many Requests";

    public static final String UNPROCESSABLE_ENTITY = "422";
    public static final String UNPROCESSABLE_ENTITY_VALUE = "Unprocessable Entity";


    // 5xx - Errores del servidor
    public static final String INTERNAL_SERVER_ERROR = "500";
    public static final String INTERNAL_SERVER_ERROR_VALUE = "Internal Server Error";

    public static final String NOT_IMPLEMENTED = "501";
    public static final String NOT_IMPLEMENTED_VALUE = "Not Implemented";

    public static final String SERVICE_UNAVAILABLE = "503";
    public static final String SERVICE_UNAVAILABLE_VALUE = "Service Unavailable";
}

