package com.iot.controllers;

import com.iot.models.dto.AccessResponseDto;
import com.iot.models.dto.AccessValidationResponseDto;
import com.iot.models.dto.CreateAccessDto;
import com.iot.services.AccessService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST API for {@code "/api/acceso"}. Delegates to {@link AccessService}; no business rules or PIN
 * verification here.
 */
@RestController
@RequestMapping("/api/acceso")
public class AccessController {

  private final AccessService accessService;

  public AccessController(AccessService accessService) {
    this.accessService = accessService;
  }

  /**
   * Receives an access attempt from the ESP32 keypad. Delegates dynamic evaluation of the PIN and
   * returns the anonymized log entry.
   */
  @PostMapping(
      value = "/validar",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AccessValidationResponseDto> processAttempt(
      @Valid @RequestBody CreateAccessDto dto) {
    // Returns Dto response.
    AccessValidationResponseDto response =
        accessService.processAccessRequest(dto.getPinIngresado());

    return ResponseEntity.status(response.getHttpStatusCode())
        .body(response); // returns dynamic status code.
  }

  /** Lists the access historical logs for auditing. */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<AccessResponseDto> listLogs() {
    return accessService.findAllLogs();
  }
}
