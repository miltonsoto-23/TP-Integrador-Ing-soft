package com.iot.observers;

import com.iot.models.dto.SensorRecordingDto;
import com.iot.models.entities.SensorRecording;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ExtremeValuesObserver implements TelemetryObserver<SensorRecording> {

    private static final Logger logger = LoggerFactory.getLogger(ExtremeValuesObserver.class);

    // Threshold constants cambiadas a Double para hacer match exacto con la entidad
    private static final Double TEMP_EMERGENCY_THRESHOLD = 50.0;
    private static final Double CO_EMERGENCY_THRESHOLD = 80.0;

    @Override
    public void update(SensorRecording record) {
        // Validación de nulos por seguridad antes de comparar
        if (record == null || record.getTemperature() == null || record.getSmoke() == null) {
            return;
        }

        // limit evaluation
        boolean tempCritica = record.getTemperature() > TEMP_EMERGENCY_THRESHOLD;
        boolean coCritico = record.getSmoke() > CO_EMERGENCY_THRESHOLD;

        // alert trigger
        if (tempCritica || coCritico) {
            this.emitAlert(record, tempCritica, coCritico);
        }
    }

    private void emitAlert(SensorRecording record, boolean tempCritica, boolean coCritico) {
        // DTO conversion
        SensorRecordingDto alertPayload = SensorRecordingDto.fromEntity(record);

        // Formateo de valores actuales
        String detalleValores = String.format("Temp: %.1f°C | Humo/CO: %.1f%%",
                alertPayload.getTemperature(),
                alertPayload.getSmoke());

        // Both parameters are critical.
        if (tempCritica && coCritico) {
            logger.error("[ALERTA AMBIENTAL CRÍTICA] - EMERGENCIA: Se superaron los niveles seguros de Temperatura (>{}°C) y Monóxido de Carbono (>{}%) | {}",
                    TEMP_EMERGENCY_THRESHOLD, CO_EMERGENCY_THRESHOLD, detalleValores);
        }
        // Only critical temperature
        else if (tempCritica) {
            logger.warn("[ALERTA AMBIENTAL] - VALOR EXTREMO: Incendio o sobrecalentamiento detectado (Temperatura >{}°C) | {}",
                    TEMP_EMERGENCY_THRESHOLD, detalleValores);
        }
        // Only CO critical levels
        else {
            logger.warn("[ALERTA AMBIENTAL] - VALOR EXTREMO: Peligro de intoxicación (Monóxido de Carbono >{}%) | {}",
                    CO_EMERGENCY_THRESHOLD, detalleValores);
        }
    }
}