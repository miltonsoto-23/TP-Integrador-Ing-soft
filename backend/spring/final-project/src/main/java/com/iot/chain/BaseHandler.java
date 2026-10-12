package com.iot.chain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public abstract class BaseHandler implements Handler {

  private Handler next;

  @Override
  @SuppressFBWarnings(
      value = "EI_EXPOSE_REP2",
      justification = "Chain of responsibility handlers are managed singletons")
  public void setNext(Handler handler) {
    this.next = handler;
  }

  @Override
  public boolean handle(AccessContext context) {

    if (next == null) {
      return true;
    }

    return next.handle(context);
  }
}
