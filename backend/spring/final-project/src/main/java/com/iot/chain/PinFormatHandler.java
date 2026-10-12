package com.iot.chain;

public class PinFormatHandler extends BaseHandler {
  // Injecting next link to the handler
  @Override
  public boolean handle(AccessContext context) {

    String pin = context.getPin();

    if (pin == null || !pin.matches("\\d{4}")) {
      context.setSuccessful(false);
      context.setErrorMessage("El campo 'pinIngresado' es obligatorio y debe tener 4 dígitos.");
      context.setHttpStatusCode(400); // Bad Request
      return false;
    }

    return super.handle(context);
  }
}
