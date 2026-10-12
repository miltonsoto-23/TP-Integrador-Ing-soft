package com.iot.services;

import com.iot.models.dto.EnvironmentalDto;
import com.iot.models.dto.SensorRecordingDto;
import com.iot.observers.TelemetryObserver;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SensorMonitor {

    private final List<TelemetryObserver<SensorRecordingDto>> observers = new ArrayList<>();

    public void addObserver(TelemetryObserver<SensorRecordingDto> observer) {

        observers.add(observer);
    }

    public void procesarAmbiental(SensorRecordingDto reading) {

        notifyAllObservers(reading);
    }

    private void notifyAllObservers(SensorRecordingDto reading) {

        for (TelemetryObserver<SensorRecordingDto> observer : observers) {
            observer.update(reading);
        }
    }
}
