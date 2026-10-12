import { create } from "zustand";

interface AlertStore {
    open: boolean;
    title: string;
    message: string;
    type: "warning" | "error";
    show: (title: string, message: string, type?: "warning" | "error") => void;
    hide: () => void;
    // Disparadores semánticos globales
    triggerPinAlert: () => void;
    triggerAnomalyAlert: () => void;
}

<<<<<<< HEAD
// SOLUCIÓN AL ENTORNO: Next.js corre en servidor y cliente. 
// Usar 'any' evita que choque el NodeJS.Timeout del backend con el del navegador.
let alertTimer: any = null;
=======
let alertTimer: NodeJS.Timeout | null = null;
>>>>>>> development

export const useAlertStore = create<AlertStore>((set) => ({
    open: false,
    title: "",
    message: "",
    type: "warning",

<<<<<<< HEAD
    // Agregamos el tipado explícito en los parámetros de la función interna
    show: (title: string, message: string, type: "warning" | "error" = "warning") => {
=======
    show: (title, message, type = "warning") => {
>>>>>>> development
        if (alertTimer) clearTimeout(alertTimer);
        set({ open: true, title, message, type });
        alertTimer = setTimeout(() => set({ open: false }), 1500);
    },

    hide: () => {
        if (alertTimer) clearTimeout(alertTimer);
        set({ open: false });
    },

<<<<<<< HEAD
    // El Frontend no necesita saber de dónde viene el ping, solo expone este método:
=======
  // El Frontend no necesita saber de dónde viene el ping, solo expone este método:
>>>>>>> development
    triggerPinAlert: () => {
        if (alertTimer) clearTimeout(alertTimer);
        set({ open: true, title: "Firmware Event", message: "Pin Detected!", type: "warning" });
        alertTimer = setTimeout(() => set({ open: false }), 1500);
    },

<<<<<<< HEAD
    // El Frontend expone este método para cuando el backend detecte anomalías:
=======
  // El Frontend expone este método para cuando el backend detecte anomalías:
>>>>>>> development
    triggerAnomalyAlert: () => {
        if (alertTimer) clearTimeout(alertTimer);
        set({ open: true, title: "Backend Alert", message: "Anomalous Data Received!", type: "error" });
        alertTimer = setTimeout(() => set({ open: false }), 1500);
    }
}));