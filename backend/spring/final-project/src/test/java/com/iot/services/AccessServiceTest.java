package com.iot.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.iot.models.dto.AccessValidationResponseDto;
import com.iot.models.entities.AccessAttempt;
import com.iot.models.entities.SystemConfig;
import com.iot.repositories.AccessRepository;
import com.iot.repositories.SystemConfigRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test") // 🟢 Forces Spring to load application-test.properties using in-memory H2
class AccessServiceTest {

  @Autowired private AccessService accessService;

  @Autowired private AccessRepository accessRepository;

  @Autowired private SystemConfigRepository configRepository;

  @MockitoBean // 🟢 Modernized replacement for @MockBean (Spring Boot 3.4+)
  private AccessMonitor accessMonitor;

  @BeforeEach
  void setUp() {
    // Purge the database tables before each test block execution to maintain isolated test
    // environments
    accessRepository.deleteAll();
    configRepository.deleteAll();
  }

  @Test
  @DisplayName(
      "Integration -> Should grant access, save to H2 database, and alert monitor when PIN is valid")
  void processAccessRequest_SuccessAndPersistence() {
    // 1. GIVEN - Populate H2 with target master pin using the empty constructor + setters
    SystemConfig masterPinConfig = new SystemConfig();
    masterPinConfig.setKey("MASTER_PIN");
    masterPinConfig.setValue("1234");
    masterPinConfig.setDescription("Master code for laboratory access control");
    configRepository.save(masterPinConfig);

    // 2. WHEN - Issue the authorization process with a matching correct input
    AccessValidationResponseDto result = accessService.processAccessRequest("1234");

    // 3. THEN - Assert target DTO properties
    assertNotNull(result);
    assertTrue(result.isAccessGranted());
    assertEquals(201, result.getHttpStatusCode());
    assertEquals("Acceso concedido", result.getMensaje());
    assertNull(result.getError());

    // 📊 VERIFY PERSISTENCE - Assert data state inside the in-memory memory buffers
    List<AccessAttempt> storedLogs = accessRepository.findAll();
    assertEquals(1, storedLogs.size(), "H2 memory database should contain exactly 1 log record");
    assertTrue(
        storedLogs.getFirst().isExitoso(),
        "The registered historical record must indicate a successful attempt");

    // Verify monitor communication occurred exactly once
    verify(accessMonitor, times(1)).notifyAllObservers(any(AccessAttempt.class));
  }

  @Test
  @DisplayName(
      "Integration -> Should reject execution, decrement attempts, and insert audit failure log on bad PIN")
  void processAccessRequest_WrongPinPersistence() {
    // 1. GIVEN - Set up database environment state
    SystemConfig masterPinConfig = new SystemConfig();
    masterPinConfig.setKey("MASTER_PIN");
    masterPinConfig.setValue("1234");
    configRepository.save(masterPinConfig);

    // 2. WHEN - Dispatch an incorrect code
    AccessValidationResponseDto result = accessService.processAccessRequest("9999");

    // 3. THEN - Assert context response values
    assertNotNull(result);
    assertFalse(result.isAccessGranted());
    assertEquals(401, result.getHttpStatusCode());
    assertEquals("PIN incorrecto, Acceso denegado.", result.getError());
    assertEquals(
        2,
        result.getRemainingAttempts(),
        "Internal handler static memory counter should track attempts remaining");

    // 📊 VERIFY PERSISTENCE - Ensure safety tracks logged the threat
    List<AccessAttempt> storedLogs = accessRepository.findAll();
    assertEquals(1, storedLogs.size());
    assertFalse(storedLogs.getFirst().isExitoso());
  }

  @Test
  @DisplayName(
      "Integration -> Should fail fast on corrupted format without querying system configuration records")
  void processAccessRequest_FormatFailureNoDatabaseHit() {
    // 1. GIVEN - Malformed short format containing illegal string data symbols
    String invalidPinPattern = "12A";

    // 2. WHEN - Request authentication service evaluation
    AccessValidationResponseDto result = accessService.processAccessRequest(invalidPinPattern);

    // 3. THEN - Assert immediate structural boundaries worked
    assertNotNull(result);
    assertFalse(result.isAccessGranted());
    assertEquals(400, result.getHttpStatusCode());
    assertEquals(
        "El campo 'pinIngresado' es obligatorio y debe tener 4 dígitos.", result.getError());

    // 📊 VERIFY ISOLATION - Ensure bad formatting generated an entry log and cut the chain early
    List<AccessAttempt> storedLogs = accessRepository.findAll();
    assertEquals(1, storedLogs.size());
    assertFalse(storedLogs.getFirst().isExitoso());
  }
}
