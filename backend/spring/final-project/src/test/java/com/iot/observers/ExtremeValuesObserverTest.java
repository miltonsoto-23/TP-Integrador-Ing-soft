package com.iot.observers;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.iot.models.entities.SensorRecording;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExtremeValuesObserverTest {

    private ExtremeValuesObserver observer;
    private ListAppender<ILoggingEvent> listAppender;
    private Logger logger;

    @BeforeEach
    void setUp() {
        observer = new ExtremeValuesObserver();

        // Configuramos el capturador de logs para la clase bajo prueba
        logger = (Logger) LoggerFactory.getLogger(ExtremeValuesObserver.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }

    @AfterEach
    void tearDown() {
        listAppender.stop();
        logger.detachAppender(listAppender);
    }

    @Test
    @DisplayName("Debería ignorar la grabación si es nula o contiene campos críticos nulos")
    void shouldIgnoreNullRecordOrNullFields() {
        // Test con registro completamente nulo
        assertDoesNotThrow(() -> observer.update(null));
        assertTrue(listAppender.list.isEmpty());

        // Test con campos nulos
        SensorRecording recordConNulos = new SensorRecording();
        recordConNulos.setTemperature(null);
        recordConNulos.setSmoke(null);

        observer.update(recordConNulos);
        assertTrue(listAppender.list.isEmpty());
    }

    @Test
    @DisplayName("Debería ignorar lecturas que estén dentro de los límites seguros")
    void shouldNotLogWhenValuesAreWithinSafeLimits() {
        SensorRecording safeRecord = new SensorRecording();
        safeRecord.setTemperature(25.0); // <= 50.0
        safeRecord.setSmoke(10.0);       // <= 80.0

        observer.update(safeRecord);

        assertTrue(listAppender.list.isEmpty(), "No debería haberse generado ningún log de alerta.");
    }

    @Test
    @DisplayName("Debería disparar WARN cuando solo la temperatura supera el umbral")
    void shouldLogWarnWhenOnlyTemperatureIsExtreme() {
        SensorRecording extremeTemp = new SensorRecording();
        extremeTemp.setTemperature(50.1); // > 50.0
        extremeTemp.setSmoke(30.0);

        observer.update(extremeTemp);

        List<ILoggingEvent> logs = listAppender.list;
        assertEquals(1, logs.size());
        assertEquals(Level.WARN, logs.get(0).getLevel());
        assertTrue(logs.get(0).getFormattedMessage().contains("[ALERTA AMBIENTAL] - VALOR EXTREMO: Incendio o sobrecalentamiento"));
        assertTrue(logs.get(0).getFormattedMessage().contains("Temp: 50,1°C"));
    }

    @Test
    @DisplayName("Debería disparar WARN cuando solo el humo/CO supera el umbral")
    void shouldLogWarnWhenOnlySmokeIsExtreme() {
        SensorRecording extremeSmoke = new SensorRecording();
        extremeSmoke.setTemperature(20.0);
        extremeSmoke.setSmoke(80.5); // > 80.0

        observer.update(extremeSmoke);

        List<ILoggingEvent> logs = listAppender.list;
        assertEquals(1, logs.size());
        assertEquals(Level.WARN, logs.get(0).getLevel());
        assertTrue(logs.get(0).getFormattedMessage().contains("[ALERTA AMBIENTAL] - VALOR EXTREMO: Peligro de intoxicación"));
        assertTrue(logs.get(0).getFormattedMessage().contains("Humo/CO: 80,5%"));
    }

    @Test
    @DisplayName("Debería disparar ERROR cuando ambos parámetros son críticos simultáneamente")
    void shouldLogErrorWhenBothValuesAreExtreme() {
        SensorRecording totalEmergency = new SensorRecording();
        totalEmergency.setTemperature(60.0); // > 50.0
        totalEmergency.setSmoke(95.0);       // > 80.0

        observer.update(totalEmergency);

        List<ILoggingEvent> logs = listAppender.list;
        assertEquals(1, logs.size());
        assertEquals(Level.ERROR, logs.get(0).getLevel());
        assertTrue(logs.get(0).getFormattedMessage().contains("[ALERTA AMBIENTAL CRÍTICA] - EMERGENCIA"));
        assertTrue(logs.get(0).getFormattedMessage().contains("Temp: 60,0°C"));
        assertTrue(logs.get(0).getFormattedMessage().contains("Humo/CO: 95,0%"));
    }
}