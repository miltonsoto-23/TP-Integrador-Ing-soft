package com.iot.models.dto;

import com.iot.models.entities.SensorReading;
import com.iot.models.entities.SensorRecording;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorRecordingDto {


    private Integer id;
    private String sensor1Id; // Ej: "LM35"
    private String sensor2Id; // Ej: "MQ7"
    @Getter
    private Double temperature;
    @Getter
    private Double smoke; // Reemplaza a "humidity" para el sensor de gas

    public static SensorRecordingDto fromEntity(SensorRecording entity) {
        if (entity == null) return null;

        return SensorRecordingDto.builder()
                .sensor1Id(entity.getSensor1Id())
                .sensor2Id(entity.getSensor2Id())
                .temperature(entity.getTemperature())
                .smoke(entity.getSmoke())
                .build();
    }

}