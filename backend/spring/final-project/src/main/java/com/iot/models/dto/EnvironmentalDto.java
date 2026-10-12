package com.iot.models.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EnvironmentalDto {

  private Double temperature;
  private Double smoke;
  private Instant timestamp;
}
