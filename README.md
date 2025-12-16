# Predicción de Retrasos de Vuelos – Back-End
# Descripción del proyecto

Este repositorio contiene el desarrollo del Back-End del proyecto Predicción de Retrasos de Vuelos. Su función principal es exponer la capacidad predictiva del modelo de Data Science mediante una API REST accesible en tiempo real.

La API permite que aplicaciones externas envíen información de un vuelo y reciban una predicción clara sobre su posible retraso, junto con la probabilidad asociada.

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

La API recibe los datos del vuelo, valida la información y consulta el modelo entrenado. Como resultado, devuelve una respuesta en formato JSON que indica si el vuelo será puntual o retrasado.

# Entregables

API REST funcional.

Documentación básica de uso.

Ejemplos de solicitudes y respuestas.

# Tecnologías utilizadas

Java, Spring Boot y APIs REST.
