package com.iot.services.strategies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

public class MeanStrategyTest {

  @Test
  void shouldCalculateMeanCorrectly() {
    MeanStrategy strategy = new MeanStrategy();

    Double result = strategy.calculate(List.of(2.0, 4.0, 6.0));

    assertEquals(4.0, result);
  }

  @Test
  void shouldCalculateMeanWithSingleValue() {
    MeanStrategy strategy = new MeanStrategy();

    Double result = strategy.calculate(List.of(7.0));

    assertEquals(7.0, result);
  }

  @Test
  void shouldCalculateMeanWithNegativeNumbers() {
    MeanStrategy strategy = new MeanStrategy();

    Double result = strategy.calculate(List.of(-2.0, -4.0, -6.0));

    assertEquals(-4.0, result);
  }

  @Test
  void shouldCalculateMeanWithMixedNumbers() {
    MeanStrategy strategy = new MeanStrategy();

    Double result = strategy.calculate(List.of(-2.0, 4.0, 8.0));

    assertEquals(10.0 / 3.0, result, 0.0001);
  }

  @Test
  void shouldThrowExceptionWhenListIsEmpty() {
    MeanStrategy strategy = new MeanStrategy();

    assertThrows(IllegalArgumentException.class, () -> strategy.calculate(List.of()));
  }
}
