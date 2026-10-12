
'use client';
//src/app/page.tsx
import { useState } from 'react';
import { useRouter } from 'next/navigation'; // El motor de navegación de Next.js
import Header from '@/components/Header';    // Ruta corregida a tu nueva estructura
import Footer from '@/components/Footer';    // Ruta corregida a tu nueva estructura

export default function Home() {
  const router = useRouter();
  
  // Como estamos en la raíz (Login), por defecto isAuthenticated siempre arranca en false
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
/*
const handleLogin = (user: string, password: string) => {

if (user.trim() === 'admin' && password === '1234') {

alert('WELCOME TO THE SYSTEM!');

setIsAuthenticated(true);


Enviamos al usuario a la pantalla de sensores (esto estaba en //)

router.push('/dashboard');

} else {

alert('Your User/Password is wrong, please try again!');

}

};
*/
  // Función de Login en estricto inglés conectada a la convención acordada
  const handleLogin = (user: string, password: string) => {
    // Cambiado '1234' por 'admin' debido a que el backend no fue entregado
    if (user.trim() === 'admin' && password.trim() === 'admin') {
      alert('WELCOME TO THE SYSTEM (EMERGENCY BYPASS ACTIVE)!');
      
      // 1. Cambiamos el estado local
      setIsAuthenticated(true);
      
      // 2. Si usás Zustand para guardar el token de sesión, agregá la línea acá:
      // useAuthStore.getState().setToken('fake-jwt-token-admin-2026');
      
      // 3. Enviamos al usuario a la pantalla de sensores
      router.push('/dashboard');
    } else {
      alert('Your User/Password is wrong, please try again!');
    }
  };

  return (
    <div className="w-full flex flex-col min-h-screen">
      {/* Pasamos las propiedades requeridas por tu nuevo Header */}
      <Header 
        isAuthenticated={isAuthenticated} 
        activeSensor="none" // En el login no hay sensor activo
        onLogin={handleLogin}
        onLogout={() => setIsAuthenticated(false)}
        onSelectSensor={() => {}} // Función vacía temporal para el login
      />

      {/* Contenedor central de bienvenida o background de la landing page */}
      <main className="flex-grow flex flex-col items-center justify-center p-6 text-center">
        <div className="max-w-md p-8 bg-white border border-gray-200 rounded-lg shadow-sm animate-fade-in">
          <h2 className="text-2xl font-bold text-gray-800 mb-2">
            Telemetría de Sensores
          </h2>
          <p className="text-sm text-gray-500">
            Por favor, ingrese sus credenciales en la barra superior para acceder al panel de control de hardware en tiempo real.
          </p>
        </div>
      </main>

      {/* Footer modular */}
      <Footer />
    </div>
  );
}