package com.iot.observers;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.iot.models.entities.AccessAttempt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccessAuditObserverTest {

    private AccessAuditObserver observer;
    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {
        observer = new AccessAuditObserver();

        // CORRECCIÓN AQUÍ: Apuntamos el espía a la clase exacta del Observer
        Logger logger = (Logger) LoggerFactory.getLogger(AccessAuditObserver.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }

    @Test
    void whenAccessAttemptFails_shouldLogSecurityAlert() {
        // Arrange: Build a failed access attempt
        AccessAttempt attempt = AccessAttempt.builder()
                .id(101)
                .exitoso(false)
                .timestamp(Instant.now())
                .build();

        // Act
        observer.update(attempt);

        // Assert
        assertFalse(listAppender.list.isEmpty(), "A security log entry should be recorded for failed attempts");
        String logMessage = listAppender.list.get(0).getFormattedMessage();
        assertTrue(logMessage.contains("[ALERTA SEGURIDAD]"));
        assertTrue(logMessage.contains("Intento de acceso fallido"));
    }

    @Test
    void whenAccessAttemptSucceeds_shouldNotLogAnything() {
        // Arrange: Build a successful access attempt
        AccessAttempt attempt = AccessAttempt.builder()
                .id(102)
                .exitoso(true)
                .timestamp(Instant.now())
                .build();

        // Act
        observer.update(attempt);

        // Assert
        assertTrue(listAppender.list.isEmpty(), "No security logs should be generated for successful entries");
    }
}