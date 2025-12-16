# Predicción de Retrasos de Vuelos
# Descripción del proyecto

Predicción de Retrasos de Vuelos es un proyecto cuyo objetivo es estimar si un vuelo será puntual o sufrirá un retraso antes de su despegue, utilizando datos históricos de vuelos. A partir de información como aerolínea, aeropuerto de origen y destino, fecha y hora de salida y distancia del vuelo, el sistema genera una predicción que permite anticiparse a posibles problemas operativos.

Los retrasos aéreos afectan a pasajeros, aerolíneas y aeropuertos, generando insatisfacción, costos adicionales y dificultades logísticas. Este proyecto busca ofrecer una solución predictiva que ayude a mejorar la planificación y la toma de decisiones, desarrollándose como un producto mínimo viable que integra ciencia de datos, Back-End y Front-End.

# Objetivo general

Desarrollar un sistema que reciba información de un vuelo y devuelva si probablemente será puntual o retrasado, junto con la probabilidad asociada a dicha predicción.

# Alcance del proyecto

El proyecto se enfoca en una clasificación binaria de vuelos:

Puntual

Retrasado

Se prioriza un enfoque simple, claro y funcional, adecuado para un MVP académico y demostrativo.

# Arquitectura del sistema

La solución está compuesta por tres áreas principales que trabajan de forma integrada:

Data Science

Back-End

Front-End

Cada área cumple un rol específico dentro del flujo general del sistema.

# Área de Back-End
Descripción

El área de Back-End desarrolla una API REST que expone la funcionalidad de predicción del modelo de Data Science. Esta API permite que otros sistemas consulten, de manera estructurada, si un vuelo tiene riesgo de retraso.

La API recibe los datos del vuelo, valida la información de entrada y devuelve una respuesta clara con la predicción y la probabilidad asociada.

# Objetivo

Proveer un servicio REST que permita consultar predicciones de retrasos de vuelos en tiempo real.

# Funcionalidades

Recepción de datos del vuelo.

Validación de campos obligatorios.

Integración con el modelo predictivo.

Manejo de errores.

Respuestas estandarizadas en formato JSON.

# Entregables

API REST funcional.

Documentación básica de uso.

Ejemplos de solicitudes y respuestas.
