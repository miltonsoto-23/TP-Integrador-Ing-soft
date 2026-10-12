package com.iot.observers;

import com.iot.models.entities.AccessAttempt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AccessAuditObserver implements TelemetryObserver<AccessAttempt> {
    // Inicializamos el logger estándar de SLF4J
    private static final Logger logger = LoggerFactory.getLogger(AccessAuditObserver.class);

    @Override
    public void update(AccessAttempt record) {
        // logs only if attempt was not successful
        if (!record.isExitoso()) {
            this.logAttempt(record);
        }
    }

    private void logAttempt(AccessAttempt record) {
        logger.warn("[ALERTA SEGURIDAD] - Intento de acceso fallido. ID Registro: {} | Timestamp: {}",
                record.getId() != null ? record.getId() : "N/A",
                record.getTimestamp());
    }
}
