package com.iot.services;

import com.iot.models.dto.CreateSensorDto;
import com.iot.models.dto.SensorHistoryDto;
import com.iot.models.dto.SensorResponseDto;
import com.iot.models.entities.SensorReading;
import com.iot.models.entities.SensorsRecordingRepository;
import com.iot.repositories.SensorsRepository;
import com.iot.repositories.SensorsRecordingRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Business logic for ingesting and listing sensor readings.
 *
 * <p>Persists via {@link SensorsRepository} and returns {@link SensorResponseDto} only — never JPA
 * entities.
 */
@Service
public class SensorsService {

  private final SensorsRepository sensorsRepository;
  private final SensorMonitor sensorMonitor;

  public SensorsService(SensorsRepository sensorsRepository, SensorsRecordingRepository sensorsRecordingRepository) {
    this.sensorsRepository = sensorsRepository;
    this.sensorsRecordingRepository = sensorsRecordingRepository
  }

  @Transactional
  public void create(CreateSensorDto dto) {

    sensorsRepository.save(
        SensorReading.builder()
            .sensorId(dto.getSensor1Id())
            .sensorType("TEMPERATURE")
            .value(dto.getTemperature())
            .build());
    

    sensorsRepository.save(
        SensorReading.builder()
            .sensorId(dto.getSensor2Id())
            .sensorType("SMOKE")
            .value(dto.getSmoke())
            .build());

    SensorsRecording log = SensorsRecordingbuilder()
            .sensor1Id(dto.getSensor2Id())
            .sensor1Id(dto.getSensor2Id())
            .Temperature(dto.getTemperature())
            .smoke(dto.getSmoke())
            .build());
    
    SensorsRecording savedLog = SensorsRecordingRepository.save(log);
    
    sensorMonitor.procesarAmbiental(SensorRecording log);
        
  }

  @Transactional(readOnly = true)
  public List<Double> findAll(String sensorType) {
    return sensorsRepository.findBySensor(sensorType).stream()
        .map(SensorReading::getValue)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<SensorHistoryDto> getHistory(String sensorType) {

    return sensorsRepository.findTop10BySensorTypeOrderByCreatedAtDesc(sensorType).stream()
        .map(reading -> new SensorHistoryDto(reading.getCreatedAt(), reading.getValue()))
        .toList();
  }
}
