package com.iot.controllers;

import com.iot.services.SensorsService;
import com.iot.services.StatisticsService;
import com.iot.services.strategies.MeanStrategy;
import com.iot.services.strategies.MedianStrategy;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class WebController {

  private final StatisticsService statisticsService;
  private final SensorsService sensorsService;

  @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Spring dependency injection")
  @Autowired
  public WebController(StatisticsService statisticsService, SensorsService sensorsService) {
    this.statisticsService = statisticsService;
    this.sensorsService = sensorsService;
  }

  @GetMapping("/historial")
  public Map<String, Object> getHistorial(@RequestParam String tipoSensor) {

    return Map.of("sensor", tipoSensor, "datos", sensorsService.getHistory(tipoSensor));
  }

  @GetMapping("/estadistica")
  public Object getEstadistica(@RequestParam String tipoCalculo, @RequestParam String tipoSensor) {

    if ("Median".equals(tipoCalculo)) {
      statisticsService.setStrategy(new MedianStrategy());
    } else {
      statisticsService.setStrategy(new MeanStrategy());
    }

    Double resultado = statisticsService.processData(sensorsService.findAll(tipoSensor));

    return Map.of("sensor", tipoSensor, "valor", resultado);
  }
}
