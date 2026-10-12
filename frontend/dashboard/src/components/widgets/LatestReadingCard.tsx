'use client';

import React from 'react';
import type { LatestReadingCardProps } from '@/types/sensors.types'; // Corregida la 's'

export function LatestReadingCard({ reading }: LatestReadingCardProps) {
  // Si no hay datos de lectura aún, mostramos un esqueleto limpio
  if (!reading) {
    return (
      <div className="p-4 rounded-xl border border-gray-200 bg-gray-50 animate-pulse">
        <div className="h-4 bg-gray-200 rounded w-1/3 mb-3"></div>
        <div className="h-8 bg-gray-200 rounded w-full mb-2"></div>
      </div>
    );
  }

  // Formateador interno para la fecha y hora completa de la última lectura
  const formatDateTime = (isoString: string) => {
    try {
      const date = new Date(isoString);
      return date.toLocaleString('es-AR', {
        day: '2-digit',
        month: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      });
    } catch {
      return '--/-- --:--:--';
    }
  };

  return (
    <div className="p-5 rounded-xl border border-slate-200 bg-white shadow-sm hover:shadow-md transition-shadow duration-200 flex flex-col gap-4">
      {/* Encabezado de la Tarjeta */}
      <div className="flex justify-between items-center border-b border-slate-100 pb-2">
        <div className="flex flex-col">
          <span className="text-xs font-bold text-slate-400 uppercase tracking-wider">Active Node</span>
          <h4 className="text-sm font-bold text-slate-700">{reading.sensorId}</h4>
        </div>
        <div className="text-[11px] font-medium text-slate-400 bg-slate-50 px-2 py-1 rounded border border-slate-100">
          ID: #{reading.id}
        </div>
      </div>

      {/* Grid de Variables Reales del Hardware */}
      <div className="grid grid-cols-2 gap-4">
        {/* Métrica de Temperatura */}
        <div className="flex flex-col p-3 rounded-lg bg-orange-50/50 border border-orange-100/70">
          <span className="text-xs font-semibold text-orange-700/80 mb-1">Temperature</span>
          <div className="flex items-baseline gap-1">
            <span className="text-2xl font-extrabold text-orange-600">
              {reading.temperature}
            </span>
            <span className="text-xs font-bold text-orange-500">°C</span>
          </div>
        </div>

        {/* Métrica de Humo (Reemplazando humedad) */}
        <div className="flex flex-col p-3 rounded-lg bg-slate-50 border border-slate-200/60">
          <span className="text-xs font-semibold text-slate-600/80 mb-1">Smoke Level</span>
          <div className="flex items-baseline gap-1">
            <span className="text-2xl font-extrabold text-slate-700">
              {reading.smoke}
            </span>
            <span className="text-xs font-bold text-slate-500">ppm</span>
          </div>
        </div>
      </div>

      {/* Pie de la tarjeta con el timestamp */}
      <div className="flex items-center justify-between text-[11px] text-slate-400 mt-1">
        <span className="flex items-center gap-1.5">
          <span className="h-1.5 w-1.5 rounded-full bg-emerald-500 animate-pulse" />
          Live Telemetry
        </span>
        <span className="font-mono">{formatDateTime(reading.createdAt)}</span>
      </div>
    </div>
  );
}