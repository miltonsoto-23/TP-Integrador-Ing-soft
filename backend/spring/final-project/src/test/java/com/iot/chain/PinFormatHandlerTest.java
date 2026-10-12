package com.iot.chain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PinFormatHandlerTest {

  private PinFormatHandler formatHandler;
  private Handler nextHandler;

  @BeforeEach
  void setUp() {
    formatHandler = new PinFormatHandler();

    // Creamos un mock del siguiente handler para simular el comportamiento de super.handle()
    nextHandler = mock(Handler.class);
    formatHandler.setNext(nextHandler); // Asumiendo que BaseHandler tiene un setNext()
  }

  @Test
  void shouldForwardToNextHandlerWhenPinIsValidAndNumeric() {
    // ARRANGE
    AccessContext context = new AccessContext("1234");
    when(nextHandler.handle(context)).thenReturn(true);

    // ACT
    boolean result = formatHandler.handle(context);

    // ASSERT
    assertTrue(result, "Should return true when the next handler accepts the context");
    verify(nextHandler, times(1)).handle(context);
  }

  @Test
  void shouldFailValidationWhenPinContainsLetters() {
    // ARRANGE
    AccessContext context = new AccessContext("12A4");

    // ACT
    boolean result = formatHandler.handle(context);

    // ASSERT
    assertFalse(result, "Should return false due to invalid format");
    assertFalse(context.isSuccessful());
    assertEquals(400, context.getHttpStatusCode());
    assertEquals(
        "El campo 'pinIngresado' es obligatorio y debe tener 4 dígitos.",
        context.getErrorMessage());
    verify(nextHandler, never()).handle(context);
  }

  @Test
  void shouldFailValidationWhenPinIsNull() {
    // ARRANGE
    AccessContext context = new AccessContext(null);

    // ACT
    boolean result = formatHandler.handle(context);

    // ASSERT
    assertFalse(result);
    assertFalse(context.isSuccessful());
    assertEquals(400, context.getHttpStatusCode());
    assertEquals(
        "El campo 'pinIngresado' es obligatorio y debe tener 4 dígitos.",
        context.getErrorMessage());
    verify(nextHandler, never()).handle(context);
  }
}
