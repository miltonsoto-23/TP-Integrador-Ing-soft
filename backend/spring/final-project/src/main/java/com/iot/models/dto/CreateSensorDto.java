package com.iot.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Request body for POST /api/envio (ESP32 / clients). */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSensorDto {

  @NotBlank private String sensor1Id; // Ej: "LM35"

  @NotBlank private String sensor2Id; // Ej: "MQ7"

  @NotNull private Double temperature;

  @NotNull private Double smoke; // Reemplaza a "humidity" para el sensor de gas
}
