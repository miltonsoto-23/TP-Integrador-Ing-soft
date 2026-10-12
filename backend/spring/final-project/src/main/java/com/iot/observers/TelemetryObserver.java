package com.iot.observers;

public interface TelemetryObserver<T> {
    void update(T record);

}
