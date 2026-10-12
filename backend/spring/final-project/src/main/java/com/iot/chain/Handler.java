package com.iot.chain;

public interface Handler {

  void setNext(Handler handler);

  boolean handle(AccessContext context);
}
