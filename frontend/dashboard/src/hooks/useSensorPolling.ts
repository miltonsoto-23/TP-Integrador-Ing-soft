
'use client';

import { useState, useEffect } from 'react';
import { getSensorReadings } from '@/services/sensors.service'; // Conectamos con tu servicio de datos
import type { SensorReading } from '@/types/sensors.types';

/**
 * Client hook that polls the sensors service on an interval.
 * Resolves the architecture requirement: polling logic lives outside presentation components.
 */
export function useSensorPolling(intervalMs: number = 2000) {
    const [data, setData] = useState<SensorReading[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
    let isMounted = true;

        // Función asíncrona para obtener las lecturas de los sensores
    const fetchReadings = async () => {
        try {
            const readings = await getSensorReadings();
            if (isMounted) {
                setData(readings);
                setError(null);
            }
        } catch (err: any) {
            if (isMounted) {
                setError(err.message || 'Failed to fetch sensor data');
            }
        } finally {
            if (isMounted) {
                setLoading(false);
            }
        }
    };

        // Primera llamada inmediata al montar el componente
        fetchReadings();

        // Configuramos el ciclo de polling (repetición)
        const intervalId = setInterval(fetchReadings, intervalMs);

        // Regrule de oro de useEffect: limpiamos el intervalo al desmontar
        return () => {
            isMounted = false;
            clearInterval(intervalId);
        };
        }, [intervalMs]);

    return { data, loading, error };
}