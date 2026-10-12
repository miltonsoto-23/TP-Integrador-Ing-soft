package com.iot.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessValidationResponseDto {

  private String mensaje;

  @JsonProperty("acceso")
  private boolean isAccessGranted;

  private Instant timestamp;

  private String error;

  @JsonProperty("intentosRestantes")
  private Integer remainingAttempts;

  private Instant bloqueadoHasta;

  @JsonIgnore // doesn't appear on final dto sent to esp-32
  private int httpStatusCode;
}
