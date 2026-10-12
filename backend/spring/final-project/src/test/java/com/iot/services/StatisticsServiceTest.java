package com.iot.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.iot.services.strategies.MeanStrategy;
import com.iot.services.strategies.MedianStrategy;
import java.util.List;
import org.junit.jupiter.api.Test;

public class StatisticsServiceTest {

  @Test
  void shouldUseMeanStrategy() {
    StatisticsService service = new StatisticsService();

    service.setStrategy(new MeanStrategy());

    Double result = service.processData(List.of(2.0, 4.0, 6.0));

    assertEquals(4.0, result);
  }

  @Test
  void shouldUseMedianStrategy() {
    StatisticsService service = new StatisticsService();

    service.setStrategy(new MedianStrategy());

    Double result = service.processData(List.of(1.0, 2.0, 3.0, 4.0));

    assertEquals(2.5, result);
  }

  @Test
  void shouldNoStrategyIsSet() {
    StatisticsService service = new StatisticsService();
    assertThrows(IllegalStateException.class, () -> service.processData(List.of(1.0, 2.0, 3.0)));
  }
}
