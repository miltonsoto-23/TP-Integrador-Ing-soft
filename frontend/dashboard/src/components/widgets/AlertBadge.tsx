'use client';

import React from 'react';
import type { AlertBadgeProps } from '@/types/sensors.types'; // Corregida la 's'

export function AlertBadge({ alert }: AlertBadgeProps) {
  if (!alert) return null;

  // Mapeo dinámico de estilos según la severidad exigida por la cátedra
  const severityStyles: Record<string, { bg: string; dot: string; label: string }> = {
  low: {
    bg: 'bg-yellow-50 border-yellow-200 text-yellow-800',
    dot: 'bg-yellow-500',
    label: 'Low Priority'
  },
  medium: {
    bg: 'bg-orange-50 border-orange-200 text-orange-800',
    dot: 'bg-orange-500',
    label: 'Warning'
  },
  high: {
    bg: 'bg-red-50 border-red-200 text-red-800 font-bold animate-bounce', // Un pequeño salto si es crítico
    dot: 'bg-red-500',
    label: 'Critical Alert'
  }
};

  // Obtenemos el estilo correspondiente (si viene algo raro por defecto usa 'low')
  const style = severityStyles[alert.severity] || severityStyles.low;

  // Formateador rápido para la hora de la alerta
  const formatTime = (isoString: string) => {
    try {
      return new Date(isoString).toLocaleTimeString('es-AR', {
        hour: '2-digit',
        minute: '2-digit'
      });
    } catch {
      return '--:--';
    }
  };

  return (
    <div className={`flex items-center justify-between p-3 rounded-lg border ${style.bg} transition-all duration-300 shadow-sm`}>
      <div className="flex items-center gap-3">
        {/* Indicador luminoso parpadeante */}
        <span className="relative flex h-2.5 w-2.5">
          <span className={`animate-ping absolute inline-flex h-full w-full rounded-full ${style.dot} opacity-75`}></span>
          <span className={`relative inline-flex rounded-full h-2.5 w-2.5 ${style.dot}`}></span>
        </span>
        
        <div className="flex flex-col gap-0.5">
          <span className="text-xs uppercase tracking-wider font-extrabold opacity-75">
            {style.label} — {alert.sensorId}
          </span>
          <p className="text-sm font-medium leading-tight">
            {alert.message}
          </p>
        </div>
      </div>
      
      <div className="text-xs font-mono opacity-60 bg-white/50 px-2 py-0.5 rounded border border-black/5 whitespace-nowrap ml-4">
        {formatTime(alert.triggeredAt)}
      </div>
    </div>
  );
}