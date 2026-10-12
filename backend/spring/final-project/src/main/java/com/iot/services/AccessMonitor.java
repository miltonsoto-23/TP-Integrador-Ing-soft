package com.iot.services;

import com.iot.models.entities.AccessAttempt;
import com.iot.observers.TelemetryObserver;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AccessMonitor {

  // List of observers
  private final List<TelemetryObserver<AccessAttempt>> observers = new ArrayList<>();

  // Register observer
  public void addObserver(TelemetryObserver<AccessAttempt> observer) {
    this.observers.add(observer);
  }

  // Notifying observers
  public void notifyAllObservers(AccessAttempt reading) {
    for (TelemetryObserver<AccessAttempt> observer : this.observers) {
      observer.update(reading);
    }
  }
}
