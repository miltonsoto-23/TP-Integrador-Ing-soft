package com.iot.models.entities;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

/**
 * JPA entity mapped to the {@code access_readings} table. Tracks access control attempts (via
 * keypad/PIN).
 */
@Entity
@Table(name = "access_readings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessAttempt {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "exitoso", nullable = false)
  private boolean exitoso;

  @CreationTimestamp
  @Column(name = "timestamp", nullable = false, updatable = false)
  private Instant timestamp;
}
