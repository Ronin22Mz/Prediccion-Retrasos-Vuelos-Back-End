# Predicción de Retrasos de Vuelos – Back-End

# Descripción del proyecto

Este repositorio contiene el desarrollo del Back-End del proyecto Predicción de Retrasos de Vuelos. Su función principal
es exponer la capacidad predictiva del modelo de Data Science mediante una API REST accesible en tiempo real.

La API permite que aplicaciones externas envíen información de un vuelo y reciban una predicción clara sobre su posible
retraso, junto con la probabilidad asociada.

# Objetivo

Proveer un servicio REST que permita consultar predicciones de retrasos de vuelos de forma confiable y estructurada.

# Alcance

Implementación de la API REST.
Validación de datos de entrada.
Integración con el modelo predictivo.
Manejo de errores y respuestas estandarizadas.

# Funcionalidades

Recepción de datos del vuelo mediante solicitudes HTTP.
Validación de campos obligatorios.
Consulta al modelo de predicción.
Respuesta con estado del vuelo y probabilidad.
Manejo de errores y mensajes claros.

# Flujo de funcionamiento

La API recibe los datos del vuelo, válida la información y consulta el modelo entrenado. Como resultado, devuelve una
respuesta en formato JSON que indica si el vuelo será puntual o retrasado.

# Entregables

API REST funcional.
Documentación básica de uso.
Ejemplos de solicitudes y respuestas.

## 🛠️ Tecnologías utilizadas

- **Java 21**
- **Spring Boot 3.5.9**
    - Spring Web (API REST)
    - Spring Data JPA (persistencia)
    - Spring Validation (validación de datos)
- **Spring Cloud 2025.0.1**
    - OpenFeign (comunicación entre servicios)
    - Resilience4j (Circuit Breaker)
- **Hibernate / JPA**
- **Bases de datos**
    - PostgreSQL (producción)
    - H2 (entorno de desarrollo / testing)
- **Flyway** (migraciones de base de datos)
- **Lombok** (reducción de boilerplate)
- **Springdoc OpenAPI 3** (Swagger UI)
- **Apache Commons Lang 3**
- **Apache Commons FileUpload**
- **Maven** (gestión de dependencias y build)
- **JUnit / Spring Boot Test** (testing)
- **GraalVM Native Build Tools** (soporte para compilación nativa)

## ⚙️ Requisitos previos

Antes de ejecutar el proyecto, asegúrate de contar con los siguientes requisitos instalados:

- **Java 21** o superior
- **Maven 3.8+**
- **Git**
- **Base de datos**
    - Supabase (producción)
    - PostgreSQL (desarrollo)
    - H2 (testing)
- **IDE recomendado**
    - IntelliJ IDEA
- **Navegador web**
    - Requerido para acceder a Swagger UI

## 📡 APIs principales

A continuación se describen los endpoints principales expuestos por la API.

### 🔹 Predicciones de vuelos

| Método | Endpoint          | Descripción                                    |
|--------|-------------------|------------------------------------------------|
| POST   | `/api/v1/predict` | Genera una predicción de puntualidad del vuelo |
| GET    | `/api/v1/stats`   | devuelve estadísticas agregadas                |

#### ▶️ Crear predicción

**POST** `/api/v1/predictions`

**Request body:**

```json
{
  "airline": "AA",
  "origin": "LAX",
  "destination": "JFK",
  "departureDate": "2026-01-06T14:30:00",
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

## 👥 Equipo

Equipo 38 – Backend Development

- [***Eduardo Maravilla***](https://github.com/EduardoMaravilla) – Backend Developer
- [***Luis Calegaris***](https://github.com/Calegaris) – Backend Developer
- [***Camilo Bermeo***](https://github.com/Chitiva09) – Backend Developer
- [***Diego Norberto***](https://github.com/ddnnpp) – Backend Developer
- [***Esteban Hood***](https://github.com/EHOOD50) – Backend Developer