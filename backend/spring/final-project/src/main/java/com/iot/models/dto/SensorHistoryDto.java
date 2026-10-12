package com.iot.models.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SensorHistoryDto {
  private Instant tiempo;
  private Double valor;
}
