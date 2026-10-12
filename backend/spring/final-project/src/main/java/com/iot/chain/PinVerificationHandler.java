package com.iot.chain;

import com.iot.models.entities.SystemConfig;
import com.iot.repositories.SystemConfigRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Optional;

public class PinVerificationHandler extends BaseHandler {
  private final SystemConfigRepository configRepository;

  @SuppressFBWarnings(
      value = "MS_SHOULD_BE_FINAL",
      justification = "local attempt variable, single access hardware")
  private int remainingAttempts = 3;

  public PinVerificationHandler(SystemConfigRepository configRepository) {
    this.configRepository = configRepository;
  } // Stays like this. Needs the repository.

  @Override
  public boolean handle(AccessContext context) {
    // Real time database access
    Optional<SystemConfig> configOpt = configRepository.findByKey("MASTER_PIN");

    if (configOpt.isEmpty()) { // If the pin is not configured on the database table
      context.setSuccessful(false);
      context.setErrorMessage("Error interno del servidor, intente nuevamente.");
      context.setHttpStatusCode(500);
      return false;
    }

    String masterPin = configOpt.get().getValue();

    if (!masterPin.equals(context.getPin())) {
      remainingAttempts--;

      if (remainingAttempts <= 0) {
        context.setSuccessful(false);
        context.setErrorMessage("Sistema bloqueado");
        context.setHttpStatusCode(423); // 423 Locked
        context.setRemainingAttempts(0);

        this.remainingAttempts = 3; // internal counter reset

        return false;
      } else {
        // If there is still remaining attempts left
        context.setSuccessful(false);
        context.setErrorMessage("PIN incorrecto, Acceso denegado.");
        context.setHttpStatusCode(401); // 401 Unauthorized
        context.setRemainingAttempts(remainingAttempts);

        return false;
      }
    }

    // if pin is correct
    context.setSuccessful(true);
    context.setHttpStatusCode(201); // 🟢 201 Created

    this.remainingAttempts = 3;

    return super.handle(context);
  }
}
