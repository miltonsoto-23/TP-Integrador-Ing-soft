package com.iot.services;

import com.iot.chain.AccessContext;
import com.iot.chain.Handler;
import com.iot.models.dto.AccessResponseDto;
import com.iot.models.dto.AccessValidationResponseDto;
import com.iot.models.entities.AccessAttempt;
import com.iot.repositories.AccessRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccessService {

  private final AccessRepository accessRepository;
  private final AccessMonitor accessMonitor;
  private final Handler validationChain;

  @SuppressFBWarnings(
      value = "EI_EXPOSE_REP2",
      justification = "Spring dependency injection for core singletons")
  public AccessService(
      AccessRepository accessRepository, AccessMonitor accessMonitor, Handler validationChain) {
    this.accessRepository = accessRepository;
    this.accessMonitor = accessMonitor;
    this.validationChain = validationChain;
  }

  @Transactional
  public AccessValidationResponseDto processAccessRequest(String incomingPin) {

    AccessContext context = new AccessContext(incomingPin); // Creating Context

    validationChain.handle(context); // Executing chain of validation.

    boolean isSuccessful = context.isSuccessful();

    AccessAttempt log =
        AccessAttempt.builder().exitoso(isSuccessful).timestamp(Instant.now()).build();

    AccessAttempt savedLog = accessRepository.save(log); // saves log on database

    accessMonitor.notifyAllObservers(savedLog); // Notifying observers

    AccessValidationResponseDto.AccessValidationResponseDtoBuilder dtoBuilder =
        AccessValidationResponseDto.builder()
            .isAccessGranted(context.isSuccessful())
            .timestamp(Instant.now())
            .httpStatusCode(context.getHttpStatusCode())
            .remainingAttempts(context.getRemainingAttempts());
    if (!context.isSuccessful()) {
      dtoBuilder.error(context.getErrorMessage());
    } else {
      dtoBuilder.mensaje("Acceso concedido");
    }
    return dtoBuilder.build();
  }

  @Transactional(readOnly = true)
  public List<AccessResponseDto> findAllLogs() {
    return accessRepository.findAll().stream().map(AccessResponseDto::fromEntity).toList();
  }
}
