package com.iot.services;

import com.iot.services.strategies.CalculationStrategy;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

  private CalculationStrategy strategy;

  public StatisticsService() {}

  public void setStrategy(CalculationStrategy strategy) {
    this.strategy = strategy;
  }

  public Double processData(List<Double> values) {
    if (strategy == null) {
      throw new IllegalStateException("No hay estrategia confirmada.");
    }
    return strategy.calculate(values);
  }
}
