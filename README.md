# Flight On Time - Backend API

Sistema backend para la predicción de retrasos de vuelos comerciales mediante inteligencia artificial. Esta API REST expone la capacidad predictiva de un modelo de Machine Learning, permitiendo a aplicaciones externas consultar predicciones de puntualidad de vuelos en tiempo real.

## Descripción

Flight On Time es una solución integral que integra un modelo de Data Science entrenado con datos históricos para estimar la probabilidad de retraso de vuelos comerciales. El backend actúa como intermediario entre las aplicaciones cliente y el microservicio de Data Science, proporcionando una interfaz RESTful robusta, validación de datos, persistencia de predicciones y análisis estadístico.

## Características Principales

- **Predicción en Tiempo Real**: Endpoint para obtener predicciones individuales de retraso de vuelos con probabilidad asociada
- **Procesamiento Batch**: Soporte para predicciones masivas mediante archivos CSV
- **Persistencia Automática**: Almacenamiento de todas las predicciones para auditoría y análisis histórico
- **Estadísticas Agregadas**: Endpoints para consultar métricas y análisis de predicciones históricas
- **API de Soporte Frontend**: Endpoints para obtener aerolíneas, aeropuertos, rutas y distancias
- **Integración con Data Science**: Comunicación asíncrona con microservicio de Machine Learning mediante OpenFeign
- **Resiliencia**: Implementación de Circuit Breaker con Resilience4j para manejo de fallos
- **Rate Limiting**: Control de tasa de solicitudes por IP para prevenir abuso
- **Caché Inteligente**: Implementación de caché con Caffeine para optimizar rendimiento
- **Documentación Interactiva**: Swagger UI integrado para exploración y prueba de endpoints
- **Migraciones Automáticas**: Gestión de esquema de base de datos con Flyway
- **Multiperfil**: Configuraciones separadas para desarrollo, testing, producción y Docker Compose

## Stack Tecnológico

### Core Framework
- **Java 21**: Lenguaje de programación
- **Spring Boot 3.5.9**: Framework principal
  - Spring Web (API REST)
  - Spring Data JPA (persistencia)
  - Spring Validation (validación de datos)
  - Spring Cache (caché)

### Microservicios y Resiliencia
- **Spring Cloud 2025.0.1**
  - OpenFeign (comunicación entre servicios)
  - Resilience4j (Circuit Breaker)

### Base de Datos
- **PostgreSQL**: Base de datos de producción (Supabase)
- **H2**: Base de datos en memoria para testing
- **Flyway**: Migraciones de base de datos

### Herramientas y Utilidades
- **Lombok**: Reducción de código boilerplate
- **Springdoc OpenAPI 3**: Documentación Swagger UI
- **Apache Commons Lang 3**: Utilidades de manipulación de strings
- **Apache Commons FileUpload**: Manejo de archivos multipart
- **Bucket4j**: Rate limiting por IP
- **Caffeine**: Implementación de caché

### Build y Testing
- **Maven**: Gestión de dependencias y build
- **JUnit / Spring Boot Test**: Framework de testing
- **GraalVM Native Build Tools**: Soporte para compilación nativa

## Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de contar con:

- **Java 21** o superior
- **Maven 3.8+**
- **Git**
- **Base de datos** (según el perfil):
  - PostgreSQL 18+ (desarrollo/producción)
  - H2 (testing - incluido)
- **IDE recomendado**: IntelliJ IDEA o Eclipse
- **Navegador web**: Para acceder a Swagger UI
- **Docker y Docker Compose** (opcional): Para ejecución con contenedores

## Instalación y Configuración

### Clonar el Repositorio

```bash
git clone <repository-url>
cd Prediccion-Retrasos-Vuelos-Back-End
```

### Configuración de Base de Datos

#### Desarrollo Local (PostgreSQL)

- Crear base de datos PostgreSQL:
  ```sql
    CREATE DATABASE prediction_db;
  ```
- Configurar variables de entorno:
  ```bash
  export DB_USERNAME=postgres
  export DB_PASSWORD=tu_password
  ```

#### Testing (H2)

H2 se configura automáticamente cuando se usa el perfil `test`. No requiere configuración adicional.

### Variables de Entorno

El proyecto utiliza variables de entorno para configuración sensible. Configura las siguientes variables según tu entorno:

```bash
# Base de datos
DB_USERNAME=postgres
DB_PASSWORD=tu_password

# Data Science API
DATA_SCIENCE_API_PREDICTION=http://localhost:8000

# Caché
CACHE_MAX_SIZE=1000
CACHE_TTL=10m

# Rate Limiting
RATE_LIMITER_VALUE=60

# Swagger
SWAGGER_IU_CONNECTION=/docs/swagger-ui.html
SPRING_DOCS_CONNECTION=/docs/api-docs
```

### Compilar el Proyecto

```bash
mvn clean install
```

## Ejecución

### Desarrollo Local

```bash
mvn spring-boot:run
```

O ejecutar directamente el JAR:

```bash
java -jar target/flight-on-time-0.0.1-SNAPSHOT.jar
```

La aplicación estará disponible en `http://localhost:8080`

### Con Docker Compose

```bash
docker-compose up -d
```

La aplicación estará disponible en `http://localhost:9090`

### Perfiles Disponibles

El proyecto soporta múltiples perfiles de Spring:

- **dev**: Desarrollo local con PostgreSQL
- **test**: Testing con H2 en memoria
- **prod**: Producción con Supabase
- **compose**: Configuración para Docker Compose

Para especificar un perfil:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Documentación de API

Una vez que la aplicación esté ejecutándose, accede a la documentación interactiva:

- **Swagger UI**: `http://localhost:8080/docs/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/docs/api-docs`

## Endpoints Principales

### API v1 - Predicciones

#### Predicción Individual
**POST** `/api/v1/flights/predict`

Genera una predicción de puntualidad para un vuelo específico.

**Request Body:**
```json
{
  "airline": "AA",
  "origin": "LAX",
  "destination": "JFK",
  "departureDate": "2026-01-06",
  "departureHour": "10:25",
  "arrivedHour": "11:30",
  "distanceKm": 3983.0
}
```

**Response:**
```json
{
  "forecast": "ON_TIME",
  "probability": 0.82
}
```

#### Predicción Batch (CSV)
**POST** `/api/v1/flights/batch-csv`

Procesa múltiples predicciones desde un archivo CSV.

**Request:**
- Content-Type: `multipart/form-data`
- Body: Archivo CSV con columnas: `airline,origin,destination,departureDate,departureHour,arrivedHour,distanceKm`

**Response:**
```json
{
  "predictions": ["..."],
  "totalProcessed": 100,
  "successful": 98,
  "failed": 2
}
```

### API v2 - Soporte Frontend

#### Obtener Aerolíneas
**GET** `/api/v2/flights/airlines`

Retorna todas las aerolíneas disponibles en el sistema.

#### Obtener Orígenes por Aerolínea
**GET** `/api/v2/flights/origins-by-airline/{idAirline}`

Retorna los aeropuertos de origen disponibles para una aerolínea específica.

#### Obtener Destinos por Aerolínea y Origen
**GET** `/api/v2/flights/destinations-by-airline/{idAirline}/{idOrigin}`

Retorna los aeropuertos de destino disponibles para una combinación aerolínea-origen.

#### Obtener Distancia entre Aeropuertos
**GET** `/api/v2/flights/distance/{idOrigin}/{idDestination}`

Retorna la distancia en kilómetros entre dos aeropuertos.

### API v3 - Estadísticas

#### Estadísticas Agregadas
**GET** `/api/v3/flights/stats`

Retorna estadísticas agregadas basadas en filtros proporcionados.

**Request Body:**
```json
{
  "startDate": "2025-01-01",
  "endDate": "2025-12-31",
  "airlineId": 1,
  "originId": 2,
  "destinationId": 3
}
```

#### Historial de Predicciones
**GET** `/api/v3/flights/records`

Retorna el historial paginado de todas las predicciones realizadas.

**Query Parameters:**
- `page`: Número de página (default: 0)
- `size`: Tamaño de página (default: 20)
- `sort`: Campo de ordenamiento

## Estructura del Proyecto

```
├── docker-compose.yaml          # Configuración Docker Compose
├── Dockerfile                    # Imagen Docker
├── pom.xml                       # Configuración Maven
├── README.md                     # Este archivo
│
└── src
    └── main
        ├── java
        │   └── com
        │       └── equipo_38
        │           └── flight_on_time
        │               ├── client              # Clientes Feign para servicios externos
        │               ├── config              # Configuraciones (caché, rate limiting, etc.)
        │               ├── controller          # Controladores REST
        │               ├── docs                # Documentación OpenAPI
        │               ├── dto                 # Data Transfer Objects
        │               ├── exception           # Manejo de excepciones
        │               ├── mapper              # Mappers entre entidades y DTOs
        │               ├── model               # Entidades JPA
        │               ├── repository          # Repositorios Spring Data JPA
        │               ├── service             # Lógica de negocio
        │               │   └── impl            # Implementaciones de servicios
        │               ├── utils               # Utilidades
        │               └── FlightOnTimeApplication.java
        │
        └── resources
            ├── application.yaml                # Configuración base
            ├── application-dev.yaml            # Perfil desarrollo
            ├── application-prod.yaml           # Perfil producción
            ├── application-test.yaml           # Perfil testing
            ├── application-compose.yaml        # Perfil Docker Compose
            ├── db
            │   └── migration                   # Scripts Flyway
            └── messages                        # Mensajes i18n
```

## Arquitectura

El proyecto sigue una arquitectura en capas:

1. **Controller Layer**: Maneja las solicitudes HTTP y respuestas
2. **Service Layer**: Contiene la lógica de negocio
3. **Repository Layer**: Acceso a datos mediante Spring Data JPA
4. **Model Layer**: Entidades JPA que representan las tablas de base de datos
5. **DTO Layer**: Objetos de transferencia de datos para comunicación API
6. **Client Layer**: Integración con servicios externos (Data Science API)

## Características de Seguridad y Rendimiento

### Rate Limiting
El sistema implementa rate limiting por IP para prevenir abuso. El límite por defecto es de 60 solicitudes por minuto, configurable mediante la variable de entorno `RATE_LIMITER_VALUE`.

### Circuit Breaker
Implementación de Circuit Breaker con Resilience4j para el cliente de Data Science, protegiendo el sistema ante fallos del microservicio externo.

### Caché
Sistema de caché con Caffeine para optimizar consultas frecuentes. Configurable mediante variables de entorno:
- `CACHE_MAX_SIZE`: Tamaño máximo del caché (default: 1000)
- `CACHE_TTL`: Tiempo de vida de entradas en caché (default: 10 minutos)

## Testing

Ejecutar tests unitarios e integración:

```bash
mvn test
```

Para ejecutar con un perfil específico:

```bash
mvn test -Dspring-boot.run.profiles=test
```

## Docker

### Construir Imagen

```bash
docker build --platform=linux/amd64 -t flight-on-time:latest .
```

### Ejecutar con Docker Compose

```bash
docker-compose up -d
```

Para detener los contenedores:

```bash
docker-compose down
```

## Migraciones de Base de Datos

Las migraciones de base de datos se gestionan automáticamente mediante Flyway. Los scripts SQL se encuentran en `src/main/resources/db/migration/` y se ejecutan automáticamente al iniciar la aplicación.

## Contribución

Este proyecto fue desarrollado por el Equipo 38 como parte de una simulación laboral en No Country.

### Equipo de Desarrollo

- **Eduardo Maravilla** - Backend Developer ([GitHub](https://github.com/EduardoMaravilla))
- **Luis Calegaris** - Backend Developer ([GitHub](https://github.com/Calegaris))
- **Camilo Bermeo** - Backend Developer ([GitHub](https://github.com/Chitiva09))
- **Diego Norberto** - Backend Developer ([GitHub](https://github.com/ddnnpp))
- **Esteban Hood** - Backend Developer ([GitHub](https://github.com/EHOOD50))

## Licencia

Este proyecto es parte de una simulación laboral educativa.

## Soporte

Para consultas o problemas, por favor abre un issue en el repositorio del proyecto.
