package com.iot.chain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.iot.models.entities.SystemConfig;
import com.iot.repositories.SystemConfigRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PinVerificationHandlerTest {

  private PinVerificationHandler verificationHandler;
  private SystemConfigRepository configRepository;
  private Handler nextHandler;

  @BeforeEach
  void setUp() {
    // Mocking dependencies
    configRepository = mock(SystemConfigRepository.class);
    nextHandler = mock(Handler.class);

    // Instantiating the handler under test with its mocked repository
    verificationHandler = new PinVerificationHandler(configRepository);
    verificationHandler.setNext(nextHandler);
  }

  @Test
  void shouldGrantAccessWhenPinMatchesMasterPin() {
    // ARRANGE
    AccessContext context = new AccessContext("1234");
    SystemConfig masterPinConfig = new SystemConfig();
    masterPinConfig.setKey("MASTER_PIN");
    masterPinConfig.setValue("1234");
    masterPinConfig.setDescription("Master code for laboratory access control");
    configRepository.save(masterPinConfig);

    when(configRepository.findByKey("MASTER_PIN")).thenReturn(Optional.of(masterPinConfig));
    when(nextHandler.handle(context)).thenReturn(true);

    // ACT
    boolean result = verificationHandler.handle(context);

    // ASSERT
    assertTrue(result);
    assertTrue(context.isSuccessful());
    assertEquals(201, context.getHttpStatusCode());
    verify(nextHandler, times(1)).handle(context);
  }

  @Test
  void shouldReturnInternalServerErrorWhenMasterPinIsMissingInDatabase() {
    // ARRANGE
    AccessContext context = new AccessContext("1234");

    // Simulating that the database does not contain the key
    when(configRepository.findByKey("MASTER_PIN")).thenReturn(Optional.empty());

    // ACT
    boolean result = verificationHandler.handle(context);

    // ASSERT
    assertFalse(result);
    assertFalse(context.isSuccessful());
    assertEquals(500, context.getHttpStatusCode());
    assertEquals("Error interno del servidor, intente nuevamente.", context.getErrorMessage());
    verify(nextHandler, never()).handle(context);
  }

  @Test
  void shouldDecreaseAttemptsAndReturnUnauthorizedOnFirstWrongPin() {
    // ARRANGE
    AccessContext context = new AccessContext("9999"); // Wrong PIN
    SystemConfig masterPinConfig = new SystemConfig();
    masterPinConfig.setKey("MASTER_PIN");
    masterPinConfig.setValue("1234");
    masterPinConfig.setDescription("Master code for laboratory access control");
    configRepository.save(masterPinConfig);

    when(configRepository.findByKey("MASTER_PIN")).thenReturn(Optional.of(masterPinConfig));

    // ACT
    boolean result = verificationHandler.handle(context);

    // ASSERT
    assertFalse(result);
    assertFalse(context.isSuccessful());
    assertEquals(401, context.getHttpStatusCode());
    assertEquals("PIN incorrecto, Acceso denegado.", context.getErrorMessage());
    assertEquals(2, context.getRemainingAttempts()); // 3 - 1 = 2 attempts left
    verify(nextHandler, never()).handle(context);
  }

  @Test
  void shouldLockSystemAndResetInternalCounterWhenAttemptsAreExhausted() {
    // ARRANGE
    SystemConfig masterPinConfig = new SystemConfig();
    masterPinConfig.setKey("MASTER_PIN");
    masterPinConfig.setValue("1234");
    masterPinConfig.setDescription("Master code for laboratory access control");
    configRepository.save(masterPinConfig);

    when(configRepository.findByKey("MASTER_PIN")).thenReturn(Optional.of(masterPinConfig));

    // Scenario: Trigger 3 consecutive failed attempts to reach the limit
    AccessContext context1 = new AccessContext("9999");
    AccessContext context2 = new AccessContext("9999");
    AccessContext context3 = new AccessContext("9999");

    // ACT & ASSERT

    // First failure (3 -> 2 attempts)
    verificationHandler.handle(context1);
    assertEquals(2, context1.getRemainingAttempts());

    // Second failure (2 -> 1 attempt)
    verificationHandler.handle(context2);
    assertEquals(1, context2.getRemainingAttempts());

    // Third failure -> SYSTEM BLOCKED (1 -> 0 attempts)
    boolean result3 = verificationHandler.handle(context3);

    assertFalse(result3);
    assertFalse(context3.isSuccessful());
    assertEquals(423, context3.getHttpStatusCode()); // Locked
    assertEquals("Sistema bloqueado", context3.getErrorMessage());
    assertEquals(0, context3.getRemainingAttempts());
    verify(nextHandler, never()).handle(context3);
  }
}
