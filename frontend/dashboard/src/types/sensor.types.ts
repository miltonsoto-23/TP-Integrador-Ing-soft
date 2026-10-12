/**
 * This file holds all TypeScript interfaces matching the backend API response shapes.
 * All properties and types are defined in strict English for architecture guidelines.
 */

export type AlertSeverity = 'low' | 'medium' | 'high';

export interface SensorReading {
  id: number;
  sensorId: string;
  temperature: number; // Mapeado al hardware de temperatura
  smoke: number;       // Mapeado al hardware de humo (ppm, nivel o porcentaje)
  createdAt: string;   // Timestamp ISO
}

export interface Alert {
  sensorId: string;
  message: string;
  severity: AlertSeverity;
  triggeredAt: string;
}

/** Props for chart/widget components — aligned with real hardware attributes */
export interface TemperatureChartProps {
  data: SensorReading[];
}

export interface SmokeChartProps {
  data: SensorReading[];
}

export interface TrendAnalizeChartProps {
  data: SensorReading[];
}

export interface LatestReadingCardProps {
  reading: SensorReading;
}

export interface AlertBadgeProps {
  alert: Alert;
}