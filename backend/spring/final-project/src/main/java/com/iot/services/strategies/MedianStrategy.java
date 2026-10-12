package com.iot.services.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MedianStrategy implements CalculationStrategy {

  @Override
  public Double calculate(List<Double> values) {
    if (values == null || values.isEmpty()) {
      throw new IllegalArgumentException("Lista vacía");
    }

    List<Double> sortedValues = new ArrayList<>(values);
    Collections.sort(sortedValues);

    if (sortedValues.size() % 2 == 1) {
      return sortedValues.get(sortedValues.size() / 2);
    } else {
      return (sortedValues.get(sortedValues.size() / 2 - 1)
              + sortedValues.get(sortedValues.size() / 2))
          / 2.0;
    }
  }
}
