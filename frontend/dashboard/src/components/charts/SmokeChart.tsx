'use client';

import React from 'react';
import {
  AreaChart,
  Area,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer
} from 'recharts';
import type { SmokeChartProps } from '@/types/sensors.types'; // Corregida la 's' al final

export function SmokeChart({ data }: SmokeChartProps) {
  
  // Función auxiliar interna para recortar la hora ISO y que el eje X no se sature
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

  // Si no hay lecturas, mostramos un estado vacío prolijo
  if (!data || data.length === 0) {
    return (
      <div className="w-full h-64 flex items-center justify-center border border-dashed border-gray-200 rounded-lg bg-gray-50">
        <p className="text-gray-400 text-sm">No smoke telemetry data available.</p>
      </div>
    );
  }

  return (
    <div className="w-full flex flex-col gap-2">
      <div className="flex flex-col">
        <span className="text-xs font-semibold uppercase tracking-wider text-gray-400">Real-time Level</span>
        <div className="flex items-baseline gap-2">
          <span className="text-3xl font-extrabold text-slate-700">
            {data[data.length - 1]?.smoke ?? 0}
          </span>
          <span className="text-sm font-medium text-slate-400">ppm</span>
        </div>
      </div>

      <div className="w-full h-[280px] mt-4">
        <ResponsiveContainer width="100%" height="100%">
          <AreaChart
            data={data}
            margin={{ top: 10, right: 10, left: -20, bottom: 0 }}
          >
            <defs>
              {/* Degradado estético para el área del gráfico de humo */}
              <linearGradient id="smokeGradient" x1="0" y1="0" x2="0" y2="1">
                <stop offset="5%" stopColor="#64748b" stopOpacity={0.3} />
                <stop offset="95%" stopColor="#64748b" stopOpacity={0} />
              </linearGradient>
            </defs>
            
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
              domain={[0, 'auto']}
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
              formatter={(value: any) => [`${value} ppm`, 'Smoke Level']}
            />
            
            <Area
              type="monotone"
              dataKey="smoke"
              stroke="#64748b"
              strokeWidth={2}
              fillOpacity={1}
              fill="url(#smokeGradient)"
              activeDot={{ r: 5, strokeWidth: 0, fill: '#475569' }}
            />
          </AreaChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
}
