'use client';

import React from 'react';
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer
} from 'recharts';
import type { TrendAnalizeChartProps } from '@/types/sensors.types';

export function TrendAnalizeChart({ data }: TrendAnalizeChartProps) {
  
  const formatTime = (isoString: string) => {
    try {
      const date = new Date(isoString);
      return date.toLocaleTimeString('en-US', {
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false
      });
    } catch {
      return '--:--';
    }
  };

  if (!data || data.length === 0) {
    return (
      <div className="w-full h-64 flex items-center justify-center border border-dashed border-gray-200 rounded-lg bg-gray-50">
        <p className="text-gray-400 text-sm">No correlation telemetry data available.</p>
      </div>
    );
  }

  return (
    <div className="w-full flex flex-col gap-2">
      <div className="flex flex-col mb-2">
        <span className="text-xs font-semibold uppercase tracking-wider text-gray-400">Correlation Analysis</span>
        <span className="text-sm font-medium text-gray-600">Overlapping temperature and smoke signatures</span>
      </div>

      <div className="w-full h-[300px]">
        <ResponsiveContainer width="100%" height="100%">
          <LineChart
            data={data}
            margin={{ top: 10, right: -10, left: -10, bottom: 0 }}
          >
            <CartesianGrid strokeDasharray="3 3" stroke="#f1f5f9" />
            
            <XAxis 
              dataKey="createdAt" 
              tickFormatter={formatTime}
              stroke="#94a3b8"
              fontSize={11}
              tickLine={false}
              dy={10}
            />
            
            {/* Eje Izquierdo: Temperatura */}
            <YAxis 
              yAxisId="left"
              stroke="#ea580c" 
              fontSize={11}
              tickLine={false}
              domain={['auto', 'auto']}
              dx={-5}
            />
            
            {/* Eje Derecho: Humo */}
            <YAxis 
              yAxisId="right"
              orientation="right"
              stroke="#64748b" 
              fontSize={11}
              tickLine={false}
              domain={['auto', 'auto']}
              dx={5}
            />
            
            <Tooltip
              labelFormatter={(label) => `Time: ${formatTime(label)}`}
              contentStyle={{
                backgroundColor: '#ffffff',
                border: '1px solid #e2e8f0',
                borderRadius: '8px',
                fontSize: '12px',
                boxShadow: '0 2px 4px rgba(0,0,0,0.05)'
              }}
            />
            
            <Legend verticalAlign="top" height={36} iconType="circle" wrapperStyle={{ fontSize: '12px' }} />
            
            {/* Línea de Temperatura conectada al eje izquierdo */}
            <Line
              yAxisId="left"
              type="monotone"
              dataKey="temperature"
              name="Temperature (°C)"
              stroke="#ea580c"
              strokeWidth={2}
              dot={false}
              activeDot={{ r: 4 }}
            />
            
            {/* Línea de Humo conectada al eje derecho */}
            <Line
              yAxisId="right"
              type="monotone"
              dataKey="smoke"
              name="Smoke (ppm)"
              stroke="#64748b"
              strokeWidth={2}
              dot={false}
              activeDot={{ r: 4 }}
            />
          </LineChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
}
