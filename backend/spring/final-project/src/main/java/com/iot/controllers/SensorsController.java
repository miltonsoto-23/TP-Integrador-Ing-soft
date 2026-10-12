package com.iot.controllers;

import com.iot.models.dto.CreateSensorDto;
import com.iot.services.SensorsService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API for {@code /sensors}. Delegates to {@link SensorsService}; no business rules in this
 * class.
 */
@RestController
@RequestMapping("/sensors")
public class SensorsController {

  private final SensorsService sensorsService;

  public SensorsController(SensorsService sensorsService) {
    this.sensorsService = sensorsService;
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public void create(@Valid @RequestBody CreateSensorDto dto) {
    sensorsService.create(dto);
  }
}
