package com.iot.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "system_configurations") // Database table name
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfig {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "config_key", nullable = false, unique = true)
  private String key; // Name of the needed pin

  @Column(name = "config_value", nullable = false)
  private String value; // pin value

  @Column(name = "description")
  private String description; // optional description
}
