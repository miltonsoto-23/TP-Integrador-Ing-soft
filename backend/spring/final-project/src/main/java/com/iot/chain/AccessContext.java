package com.iot.chain;

import java.time.Instant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AccessContext {

  private final String pin;
  private boolean successful;
  private String errorMessage;
  private Integer remainingAttempts;
  private Instant bloqueadoHasta;
  private int httpStatusCode = 201; // Default (201 Created)
}
