package com.iot.services;

import static org.mockito.Mockito.verify;

import com.iot.models.dto.EnvironmentalDto;
import com.iot.models.dto.SensorRecordingDto;
import com.iot.observers.TelemetryObserver;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SensorMonitorTest {

    private SensorMonitor sensorMonitor;

    @Mock
    private TelemetryObserver<SensorRecordingDto> observer1;

    @Mock
    private TelemetryObserver<SensorRecordingDto> observer2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sensorMonitor = new SensorMonitor();

        sensorMonitor.addObserver(observer1);
        sensorMonitor.addObserver(observer2);
    }

    @Test
    void shouldNotifyAllObservers() {

        SensorRecordingDto reading = new SensorRecordingDto('temp', 'smoke', 25.0, 10.0, Instant.now());

        sensorMonitor.procesarAmbiental(reading);

        verify(observer1).update(reading);
        verify(observer2).update(reading);
    }
}
