package com.iot.services.strategies;

import java.util.List;

public class MeanStrategy implements CalculationStrategy {

  @Override
  public Double calculate(List<Double> values) {
    if (values == null || values.isEmpty()) {
      throw new IllegalArgumentException("Lista vacia.");
    }
    return values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
  }
}
