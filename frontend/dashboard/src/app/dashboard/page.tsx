/**
 * Dashboard route — a React Server Component. Do **not** add `'use client'`.
 *
 * Responsible for page layout and composition only: no `fetch()` or polling here.
 * Import chart and widget Client Components and arrange them on the page.
 */

import React from 'react';
import Footer from '@/components/Footer';
import DashboardController from '@/components/dashboard/DashboardController'; 
import type { Alert, SensorReading } from '@/types/sensor.types';

// Placeholders requeridos en inglés por la cátedra
const placeholderReading: SensorReading = {
  id: 0,
  sensorId: '—',
  temperature: 0,
  smoke: 0, // Mantenemos el tipado por si lo exige types/sensor.types
  createdAt: new Date().toISOString(),
};

const placeholderAlert: Alert = {
  sensorId: '—',
  message: '—',
  severity: 'low',
  triggeredAt: new Date().toISOString(),
};

export default function DashboardPage() {
  // Array de datos inicialmente vacío como pide la consigna
  const readings: SensorReading[] = [];
  return (
    <div className="bg-[#f8fafc] text-[#333] font-sans min-h-screen flex flex-col">      
      {/* Inyectamos el Controlador del Cliente. 
        Él se va a encargar de renderizar el Header reactivo, las tablas y los gráficos 
        respetando que esta página raíz se mantenga como Server Component.
      */}
      <main className="flex-grow">
        <DashboardController 
          initialReadings={readings}
          placeholderReading={placeholderReading}
          placeholderAlert={placeholderAlert}
        />
      </main>
      {/* 3. Footer fijo */}
      <Footer />
    </div>
  );
}