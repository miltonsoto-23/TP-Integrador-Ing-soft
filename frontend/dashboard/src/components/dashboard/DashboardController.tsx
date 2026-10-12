'use client';

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import Header from '@/components/Header';
import { TemperatureChart } from '@/components/charts/TemperatureChart';
import { SmokeChart } from '@/components/charts/SmokeChart';
import { TrendAnalizeChart } from '@/components/charts/TrendAnalizeChart';
import { LatestReadingCard } from '@/components/widgets/LatestReadingCard';
import { AlertBadge } from '@/components/widgets/AlertBadge';
import type { Alert, SensorReading } from '@/types/sensor.types';

// ==========================================
// 1. MAPPER para diferencias de idioma
// ==========================================
const parseBackendData = (backendResponse: any): SensorReading[] => {
    // Si viene vacío o no es el formato esperado, devolvemos array vacío (No rompe la app)
    if (!backendResponse || !Array.isArray(backendResponse.datos)) {
        return [];
    }

    return backendResponse.datos.map((item: any, index: number) => {
        const rawTime = item.tiempo ?? item.fecha ?? item.timestamp ?? new Date().toISOString();
        
        // CUMPLIMOS PUNTO (5): Extraer únicamente los segundos
        const seconds = new Date(rawTime).toLocaleTimeString([], { second: '2-digit' });

        // CUMPLIMOS PUNTO (4): Atajamos las variables en español o genéricas del backend
        const temperatureValue = item.temperatura ?? item.temperature ?? item.valor1 ?? item.temp ?? 0;
        const smokeValue = item.humo ?? item.smoke ?? item.valor2 ?? 0;

        return {
            id: item.id ?? index + 1, // CUMPLIMOS PUNTO (3): Iteración/ID automático
            sensorId: item.sensorId ?? 'Sensor-IoT',
            temperature: Number(temperatureValue),
            smoke: Number(smokeValue),
            createdAt: `${seconds}s`, // Guardamos los segundos limpios acá
        };
    });
};

interface DashboardControllerProps {
    initialReadings: SensorReading[];
    placeholderReading: SensorReading;
    placeholderAlert: Alert;
}

export default function DashboardController({
    initialReadings,
    placeholderReading,
    placeholderAlert,
}: DashboardControllerProps) {
    const router = useRouter();

    const [activeSensor, setActiveSensor] = useState<'none' | 'temperature' | 'smoke' | 'trend'>('temperature');
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(true);

    // ==========================================
    // 2. ESTADO REAL DE LAS MEDICIONES
    // ==========================================
    const [readings, setReadings] = useState<SensorReading[]>(initialReadings);

    // ==========================================
    // 3. INTERVALO DE 2 SEGUNDOS (POLLING)
    // ==========================================
    useEffect(() => {
        const fetchData = async () => {
            try {
                // TODO: Reemplazar por la URL real del backend de Java cuando esté listo
                const response = await fetch('http://localhost:8080/api/mediciones'); 
                if (!response.ok) throw new Error('Error en backend');
                
                const rawData = await response.json();
                
                // Pasamos el camión de datos por el colador del Mapper
                const cleanData = parseBackendData(rawData);
                setReadings(cleanData);
            } catch (error) {
                // Si el backend cae, mantenemos lo que tenemos o dejamos vacío de forma segura
                console.warn("Backend no disponible. Simulando o esperando re-conexión...");
            }
        };

        // Ejecutar inmediatamente y luego setear el bucle cada 2 segundos
        fetchData();
        const interval = setInterval(fetchData, 2000);

        return () => clearInterval(interval);
    }, []);

    const handleLogout = () => {
        setIsAuthenticated(false);
        router.push('/');
    };

    const handleLoginSimulated = () => {
        setIsAuthenticated(true);
    };

    // Obtenemos la última medición real para la tarjeta, si no hay usa el placeholder
    const latestReading = readings.length > 0 ? readings[readings.length - 1] : placeholderReading;

    return (
        <div className="w-full flex flex-col gap-6">
            <Header 
                isAuthenticated={isAuthenticated}
                activeSensor={activeSensor}
                onLogin={handleLoginSimulated}
                onLogout={handleLogout}
                onSelectSensor={(sensor) => setActiveSensor(sensor)}
            />

            <div className="p-6 max-w-7xl mx-auto w-full flex flex-col gap-6 mb-[100px]">

                <section className="grid gap-4 md:grid-cols-2">
                    {/* Le pasamos la última medición real filtrada por el Mapper */}
                    <LatestReadingCard reading={latestReading} />
                    <AlertBadge alert={placeholderAlert} />
                </section>

                <section className="bg-white p-6 border border-gray-200 rounded-lg shadow-sm min-h-[350px] flex flex-col justify-between">
                    <div className="border-b border-gray-100 pb-3 mb-4">
                        <h3 className="text-lg font-bold text-gray-800 capitalize">
                            Active View: {activeSensor === 'none' ? 'No Sensor Selected' : `${activeSensor} Panel`}
                        </h3>
                    </div>

                    <div className="flex-grow flex items-center justify-center">
                        {/* CUMPLIMOS PUNTO (2): Arquitectura adaptativa a la barra de sensores */}
                        {activeSensor === 'temperature' && readings.length > 0 && (
                            <div className="w-full h-full animate-fade-in">
                                <TemperatureChart data={readings} />
                            </div>
                        )}

                        {activeSensor === 'smoke' && readings.length > 0 && (
                            <div className="w-full h-full animate-fade-in">
                                <SmokeChart data={readings} />
                            </div>
                        )}

                        {activeSensor === 'trend' && readings.length > 0 && (
                            <div className="w-full h-full animate-fade-in">
                                <TrendAnalizeChart data={readings} />
                            </div>
                        )}

                        {(activeSensor === 'none' || readings.length === 0) && (
                            <p className="text-gray-400 italic">
                                {readings.length === 0 ? 'Waiting for backend data stream...' : 'Please select a sensor option from the menu above.'}
                            </p>
                        )}
                    </div>
                </section>
            </div>
        </div>
    );
}