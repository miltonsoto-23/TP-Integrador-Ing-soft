package com.iot.models.dto;

import com.iot.models.entities.AccessAttempt;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessResponseDto {

  private Integer id;
  private boolean exitoso;
  private Instant timestamp;

  public static AccessResponseDto fromEntity(AccessAttempt entity) {
    return AccessResponseDto.builder()
        .id(entity.getId())
        .exitoso(entity.isExitoso())
        .timestamp(entity.getTimestamp())
        .build();
  }
}
