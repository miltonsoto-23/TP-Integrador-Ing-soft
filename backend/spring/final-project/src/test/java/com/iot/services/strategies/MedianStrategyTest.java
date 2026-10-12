package com.iot.services.strategies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

public class MedianStrategyTest {

  @Test
  void shouldCalculateMedianForOddList() {
    MedianStrategy strategy = new MedianStrategy();

    Double result = strategy.calculate(List.of(5.0, 1.0, 3.0));

    assertEquals(3.0, result);
  }

  @Test
  void shouldCalculateMedianForEvenList() {
    MedianStrategy strategy = new MedianStrategy();

    Double result = strategy.calculate(List.of(1.0, 2.0, 3.0, 4.0));

    assertEquals(2.5, result);
  }

  @Test
  void shouldCalculateMedianWithSingleValue() {
    MedianStrategy strategy = new MedianStrategy();

    Double result = strategy.calculate(List.of(8.0));

    assertEquals(8.0, result);
  }

  @Test
  void shouldCalculateMedianWithNegativeNumbers() {
    MedianStrategy strategy = new MedianStrategy();

    Double result = strategy.calculate(List.of(-5.0, -1.0, -3.0));

    assertEquals(-3.0, result);
  }

  @Test
  void shouldCalculateMedianWithMixedNumbers() {
    MedianStrategy strategy = new MedianStrategy();

    Double result = strategy.calculate(List.of(-2.0, 8.0, 4.0));

    assertEquals(4.0, result);
  }

  @Test
  void shouldThrowExceptionWhenListIsEmpty() {
    MedianStrategy strategy = new MedianStrategy();

    assertThrows(IllegalArgumentException.class, () -> strategy.calculate(List.of()));
  }
}
