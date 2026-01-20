package com.equipo_38.flight_on_time;

import com.equipo_38.flight_on_time.dto.FlightRequestDTO;
import com.equipo_38.flight_on_time.dto.PredictionResponseDTO;
import com.equipo_38.flight_on_time.exception.ApiResponseError;
import com.equipo_38.flight_on_time.model.FlightStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@SpringBootTest(
		properties = "spring.profiles.active=test"
)
@AutoConfigureMockMvc
@DisplayName("Full Integration Tests")
class FlightOnTimeApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("Test Get Prediction")
	void testGetPrediction() throws Exception {
		FlightRequestDTO flightRequestDTO = new FlightRequestDTO("AA", "ABQ", "ATL", LocalDate.now(), LocalTime.now(), LocalTime.now().plus(2, ChronoUnit.HOURS), 1269.0);
		var response = mockMvc.perform(
				post("/api/v1/flights/predict")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(flightRequestDTO))
		).andReturn().getResponse();

		PredictionResponseDTO predictionResponseDTO = objectMapper.readValue(response.getContentAsByteArray(), PredictionResponseDTO.class);

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertEquals(FlightStatus.DELAYED, predictionResponseDTO.forecast());
		assertEquals(0.0, predictionResponseDTO.probability());
	}

	@Test
	@DisplayName("Test Failed Same City Prediction (EN)")
	void testFailedSameCityPredictionEN() throws Exception {
		FlightRequestDTO flightRequestDTO = new FlightRequestDTO("AA", "ABQ", "ABQ", LocalDate.now(), LocalTime.now(), LocalTime.now(), 1269.0);
		var response = mockMvc.perform(
				post("/api/v1/flights/predict")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(flightRequestDTO))
		).andReturn().getResponse();

		ApiResponseError apiResponseError = objectMapper.readValue(response.getContentAsByteArray(), ApiResponseError.class);

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertEquals("Validation Data Error", apiResponseError.message());
		assertEquals("Origin and destination must be different", apiResponseError.backendMessage());
	}

	@Test
	@DisplayName("Test Failed Same City Prediction (ES)")
	void testFailedSameCityPredictionES() throws Exception {
		FlightRequestDTO flightRequestDTO = new FlightRequestDTO("AA", "ABQ", "ABQ", LocalDate.now(), LocalTime.now(), LocalTime.now(), 1269.0);
		var response = mockMvc.perform(
				post("/api/v1/flights/predict")
						.header(HttpHeaders.ACCEPT_LANGUAGE, "es")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(flightRequestDTO))
		).andReturn().getResponse();

		System.out.println(response.getContentAsString());
		ApiResponseError apiResponseError = objectMapper.readValue(response.getContentAsByteArray(), ApiResponseError.class);

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertEquals("Validation Data Error", apiResponseError.message());
		assertEquals("El origen y el destino deben ser diferentes", apiResponseError.backendMessage());
	}

	@Test
	@DisplayName("Test Failed Plus One Year Prediction (EN)")
	void testFailedPlusOneYearPredictionEN() throws Exception {
		FlightRequestDTO flightRequestDTO = new FlightRequestDTO("AA", "ABQ", "ATL", LocalDate.now().plusYears(1).plusDays(1), LocalTime.now(), LocalTime.now(), 1269.0);
		var response = mockMvc.perform(
				post("/api/v1/flights/predict")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(flightRequestDTO))
		).andReturn().getResponse();

		ApiResponseError apiResponseError = objectMapper.readValue(response.getContentAsByteArray(), ApiResponseError.class);

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertEquals("Validation Data Error", apiResponseError.message());
		assertEquals("Departure date must not be more than one year ahead", apiResponseError.backendMessage());
	}

	@Test
	@DisplayName("Test Failed Plus One Year Prediction (ES)")
	void testFailedPlusOneYearPredictionES() throws Exception {
		FlightRequestDTO flightRequestDTO = new FlightRequestDTO("AA", "ABQ", "ATL", LocalDate.now().plusYears(1).plusDays(1), LocalTime.now(), LocalTime.now(), 1269.0);
		var response = mockMvc.perform(
				post("/api/v1/flights/predict")
						.header(HttpHeaders.ACCEPT_LANGUAGE, "es")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(flightRequestDTO))
		).andReturn().getResponse();

		ApiResponseError apiResponseError = objectMapper.readValue(response.getContentAsByteArray(), ApiResponseError.class);

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertEquals("Validation Data Error", apiResponseError.message());
		assertEquals("La fecha de salida no puede ser mayor a un año", apiResponseError.backendMessage());
	}

	// Tests prioritarios

	@Test
	@DisplayName("Test Failed Airline Blank (EN)")
	void testFailedAirlineBlankEN() throws Exception {
		FlightRequestDTO flightRequestDTO = new FlightRequestDTO(null, "ABQ", "ATL", LocalDate.now(), LocalTime.now(), LocalTime.now(), 1269.0);
		var response = mockMvc.perform(
				post("/api/v1/flights/predict")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(flightRequestDTO))
		).andReturn().getResponse();

		ApiResponseError apiResponseError = objectMapper.readValue(response.getContentAsByteArray(), ApiResponseError.class);

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertEquals("Validation Data Error", apiResponseError.message());
		assertEquals("Airline is required", apiResponseError.backendMessage());
	}

	@Test
	@DisplayName("Test Failed Airline Blank (ES)")
	void testFailedAirlineBlankES() throws Exception {
		FlightRequestDTO flightRequestDTO = new FlightRequestDTO(null, "ABQ", "ATL", LocalDate.now(), LocalTime.now(), LocalTime.now(), 1269.0);
		var response = mockMvc.perform(
				post("/api/v1/flights/predict")
						.header(HttpHeaders.ACCEPT_LANGUAGE, "es")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(flightRequestDTO))
		).andReturn().getResponse();

		ApiResponseError apiResponseError = objectMapper.readValue(response.getContentAsByteArray(), ApiResponseError.class);

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertEquals("Validation Data Error", apiResponseError.message());
		assertEquals("La aerolínea es obligatoria", apiResponseError.backendMessage());
	}

	@Test
	@DisplayName("Test Failed Origin Blank (EN)")
	void testFailedOriginBlankEN() throws Exception {
		FlightRequestDTO flightRequestDTO = new FlightRequestDTO("AA", null, "ATL", LocalDate.now().plusDays(1), LocalTime.now(), LocalTime.now(), 1269.0);
		var response = mockMvc.perform(
				post("/api/v1/flights/predict")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(flightRequestDTO))
		).andReturn().getResponse();

		ApiResponseError apiResponseError = objectMapper.readValue(response.getContentAsByteArray(), ApiResponseError.class);

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertEquals("Validation Data Error", apiResponseError.message());
		assertEquals("Origin airport is required", apiResponseError.backendMessage());

	}

	@Test
	@DisplayName("Test Failed Origin Blank (ES)")
	void testFailedOriginBlankES() throws Exception {
		FlightRequestDTO flightRequestDTO = new FlightRequestDTO("AA", null, "ATL", LocalDate.now().plusDays(1), LocalTime.now(), LocalTime.now(), 1269.0);
		System.out.println(objectMapper.writeValueAsString(flightRequestDTO));
		var response = mockMvc.perform(
				post("/api/v1/flights/predict")
						.header(HttpHeaders.ACCEPT_LANGUAGE, "es")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(flightRequestDTO))
		).andReturn().getResponse();

		ApiResponseError apiResponseError = objectMapper.readValue(response.getContentAsByteArray(), ApiResponseError.class);

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertEquals("Validation Data Error", apiResponseError.message());
		assertEquals("El aeropuerto de origen es obligatorio", apiResponseError.backendMessage());
	}

	@Test
	@DisplayName("Test Failed Destination Blank (EN)")
	void testFailedDestinationBlankEN() throws Exception {
		FlightRequestDTO flightRequestDTO = new FlightRequestDTO("AA", "ABQ", null, LocalDate.now().plusDays(1), LocalTime.now(), LocalTime.now(), 1269.0);
		var response = mockMvc.perform(
				post("/api/v1/flights/predict")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(flightRequestDTO))
		).andReturn().getResponse();

		ApiResponseError apiResponseError = objectMapper.readValue(response.getContentAsByteArray(), ApiResponseError.class);

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertEquals("Validation Data Error", apiResponseError.message());
		assertEquals("Destination airport is required", apiResponseError.backendMessage());
	}

	@Test
	@DisplayName("Test Failed Destination Blank (ES)")
	void testFailedDestinationBlankES() throws Exception {
		FlightRequestDTO flightRequestDTO = new FlightRequestDTO("AA", "ABQ", null, LocalDate.now().plusDays(1), LocalTime.now(), LocalTime.now(), 1269.0);
		var response = mockMvc.perform(
				post("/api/v1/flights/predict")
						.header(HttpHeaders.ACCEPT_LANGUAGE, "es")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(flightRequestDTO))
		).andReturn().getResponse();

		ApiResponseError apiResponseError = objectMapper.readValue(response.getContentAsByteArray(), ApiResponseError.class);

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertEquals("Validation Data Error", apiResponseError.message());
		assertEquals("El aeropuerto de destino es obligatorio", apiResponseError.backendMessage());
	}
}
