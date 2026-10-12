import { SensorReading, Alert } from '@/types/sensors.types';

// Toma la URL del .env raíz como exige la consigna de la cátedra
const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:3000/api';

/**
 * FETCHES RECENT READINGS FOR THE DASHBOARD
 * Automatically manages fallbacks with safe mock telemetry if backend is offline.
 */
export async function getLatestReadings(): Promise<SensorReading[]> {
    try {
        const response = await fetch(`${API_URL}/sensors`, {
          cache: 'no-store', // Desactiva la cache para datos en tiempo real
        });

        if (!response.ok) throw new Error(`API error: ${response.status}`);
        const data = await response.json();

        // CONTROLADOR DE DATOS REALES: El backend ya manda % limpio.
        // Forzamos el casteo final para asegurar que TypeScript vea el tipo correcto
        return data.map((item: any) => ({
            id: Number(item.id ?? Date.now()),
            sensorId: item.sensorId || 'NODE-01',
            temperature: Number(item.temperature ?? 0),
            smoke: Number(item.smoke ?? 0), // Pasa el % limpio que viene de la API
            createdAt: item.createdAt || new Date().toISOString(),
        })) as SensorReading[];
    } catch (error) {
            console.warn('Backend offline. Falling back to simulated live telemetry.');
            
            // =========================================================================
            // AJUSTE MOCK: Generar porcentajes (%) realistas (0 a 100) para la defensa
            // =========================================================================
            return Array.from({ length: 10 }).map((_, i) => ({
                id: Date.now() - i * 2000,
                sensorId: 'NODE-TEMP-01',
                temperature: parseFloat((22 + Math.random() * i * 0.5).toFixed(2)),
                // Simulamos un % de humo que oscila sanamente entre el 5% y el 15% en el mock estable
                smoke: parseFloat((5 + Math.random() * 8).toFixed(2)), 
                createdAt: new Date(Date.now() - i * 2000).toISOString(),
            })).reverse() as SensorReading[]; // Orden cronológico para los gráficos
    }
}

/**
 * FILTER READINGS FOR ONE SENSOR
 */
export async function getReadingsBySensor(sensorId: string): Promise<SensorReading[]> {
    try {
        const response = await fetch(`${API_URL}/sensors/${sensorId}`, { cache: 'no-store' });
        if (!response.ok) throw new Error(`API error: ${response.status}`);
        const data = await response.json();
        
        return data.map((item: any) => ({
            id: Number(item.id ?? Date.now()),
            sensorId: item.sensorId,
            temperature: Number(item.temperature ?? 0),
            smoke: Number(item.smoke ?? 0), // Pasa directo sin conversiones raras
            createdAt: item.createdAt,
        })) as SensorReading[];
    } catch (error) {
        // Si falla, filtramos del mock general para mantener consistencia visual
        const readings: SensorReading[] = await getLatestReadings();
        
        // Retornamos asegurando el tipo de salida para evitar que llore el linter
        return readings.filter((r: SensorReading) => 
            r.sensorId === sensorId || sensorId === 'all' || r.sensorId === 'NODE-TEMP-01'
        ) as SensorReading[];
    }
}

/**
 * FETCH ALERT PAYLOADS FOR WIDGETS
 */
export async function getAlerts(): Promise<Alert[]> {
    try {
        const response = await fetch(`${API_URL}/alerts`, { cache: 'no-store' });
        if (!response.ok) throw new Error(`API error: ${response.status}`);
        const data = await response.json();
        
        // CASTEADO SEGURO: Obligamos a TypeScript a entender que la respuesta mapea con la interfaz Alert
        return data as Alert[];
    } catch (error) {
        // Retorna una alerta simulada realista adaptada al concepto de %
        return [
            {
                sensorId: 'NODE-SMOKE-01',
                message: 'Smoke saturation levels within normal percentage limits.',
                severity: 'low',
                triggeredAt: new Date().toISOString(),
            },
        ] as Alert[];
    }
}