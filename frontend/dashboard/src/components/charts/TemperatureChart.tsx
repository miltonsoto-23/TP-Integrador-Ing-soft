'use client';

import React from 'react';
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer
} from 'recharts';
import type { TemperatureChartProps } from '@/types/sensors.types'; // Usamos tu nombre con 's'

export function TemperatureChart({ data }: TemperatureChartProps) {
  
  // Función auxiliar interna para formatear el eje X en formato HH:MM:SS
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
        <p className="text-gray-400 text-sm">No temperature telemetry data available.</p>
      </div>
    );
  }

  return (
    <div className="w-full flex flex-col gap-2">
      <div className="flex flex-col">
        <span className="text-xs font-semibold uppercase tracking-wider text-gray-400">Current Temperature</span>
        <div className="flex items-baseline gap-2">
          <span className="text-3xl font-extrabold text-amber-600">
            {data[data.length - 1]?.temperature ?? 0}
          </span>
          <span className="text-sm font-medium text-amber-500">°C</span>
        </div>
      </div>

      <div className="w-full h-[280px] mt-4">
        <ResponsiveContainer width="100%" height="100%">
          <LineChart
            data={data}
            margin={{ top: 10, right: 10, left: -20, bottom: 0 }}
          >
            <CartesianGrid strokeDasharray="3 3" stroke="#f1f5f9" vertical={false} />
            
            <XAxis 
              dataKey="createdAt" 
              tickFormatter={formatTime}
              stroke="#94a3b8"
              fontSize={11}
              tickLine={false}
              dy={10}
            />
            
            <YAxis 
              stroke="#94a3b8" 
              fontSize={11}
              tickLine={false}
              domain={['auto', 'auto']} // Se adapta al rango de temperatura actual
              dx={-5}
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
              formatter={(value: any) => [`${value} °C`, 'Temperature']}
            />
            
            <Line
              type="monotone"
              dataKey="temperature"
              stroke="#ea580c" // Naranja Tailwind prolijo
              strokeWidth={2.5}
              dot={{ r: 3, strokeWidth: 0, fill: '#ea580c' }}
              activeDot={{ r: 6, strokeWidth: 0, fill: '#c2410c' }}
            />
          </LineChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
}
