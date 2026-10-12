import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

// Cambiamos los metadatos para que el proyecto tenga nombre formal ante la cátedra
export const metadata: Metadata = {
  title: "TP Final - Ingeniería de Software & Hardware",
  description: "Sistema de telemetría y análisis de sensores en tiempo real",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html
      lang="es" // Cambiado a español para el idioma del navegador
      className={`${geistSans.variable} ${geistMono.variable} h-full antialiased`}
    >
      {/* Agregamos 'min-h-screen bg-[#f8fafc] text-[#333]' para asegurar 
        el color de fondo del diseño y el flujo vertical para el layout global
      */}
      <body className="min-h-screen flex flex-col bg-[#f8fafc] text-[#333]">
        {children}
      </body>
    </html>
  );
}