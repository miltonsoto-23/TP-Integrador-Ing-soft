package com.iot.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.models.dto.AccessResponseDto;
import com.iot.models.dto.AccessValidationResponseDto;
import com.iot.models.dto.CreateAccessDto;
import com.iot.services.AccessService;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AccessController.class) // Boots up ONLY the web controller layer
class AccessControllerTest {

  @Autowired private MockMvc mockMvc; // Simulates HTTP requests without starting a real server

  @MockitoBean
  private AccessService accessService; // 🟢 Mocked version of your service managed by Spring

  @Autowired private ObjectMapper objectMapper; // Serializes Java objects to JSON text

  @Test
  @DisplayName("POST /validar -> Should return 201 Created when the PIN is correct")
  void processAttempt_Success() throws Exception {
    // 1. GIVEN
    CreateAccessDto requestDto = new CreateAccessDto("1234");

    AccessValidationResponseDto mockedResponse =
        AccessValidationResponseDto.builder()
            .isAccessGranted(true)
            .mensaje("Acceso concedido")
            .remainingAttempts(3)
            .httpStatusCode(201) // The dynamic status code read by your controller
            .timestamp(Instant.now())
            .build();

    // Stubbing: When the controller invokes the service, return our predefined response
    Mockito.when(accessService.processAccessRequest("1234")).thenReturn(mockedResponse);

    // 2. WHEN / THEN
    mockMvc
        .perform(
            post("/api/acceso/validar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isCreated()) // 🟢 Expects HTTP 201
        .andExpect(jsonPath("$.acceso").value(true)) // Verifies "acceso" field in the JSON response
        .andExpect(jsonPath("$.mensaje").value("Acceso concedido"))
        .andExpect(jsonPath("$.intentosRestantes").value(3));
  }

  @Test
  @DisplayName("POST /validar -> Should return 423 Locked when the system is locked out")
  void processAttempt_Locked() throws Exception {
    // 1. GIVEN
    CreateAccessDto requestDto = new CreateAccessDto("9999");

    AccessValidationResponseDto mockedResponse =
        AccessValidationResponseDto.builder()
            .isAccessGranted(false)
            .error("Sistema bloqueado")
            .remainingAttempts(0)
            .httpStatusCode(423) // Testing the controller's ability to forward dynamic statuses
            .timestamp(Instant.now())
            .build();

    Mockito.when(accessService.processAccessRequest("9999")).thenReturn(mockedResponse);

    // 2. WHEN / THEN
    mockMvc
        .perform(
            post("/api/acceso/validar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isLocked()) // 🟢 Expects HTTP 423
        .andExpect(jsonPath("$.acceso").value(false))
        .andExpect(jsonPath("$.error").value("Sistema bloqueado"))
        .andExpect(jsonPath("$.intentosRestantes").value(0));
  }

  @Test
  @DisplayName("GET /api/acceso -> Should return the full list of historical logs")
  void listLogs_Success() throws Exception {
    // 1. GIVEN
    AccessResponseDto historicalLog = new AccessResponseDto();
    List<AccessResponseDto> mockedLogList = List.of(historicalLog);

    Mockito.when(accessService.findAllLogs()).thenReturn(mockedLogList);

    // 2. WHEN / THEN
    mockMvc
        .perform(get("/api/acceso").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()) // 🟢 Expects HTTP 200
        .andExpect(jsonPath("$").isArray()); // Asserts the root response element is a JSON array
  }
}
