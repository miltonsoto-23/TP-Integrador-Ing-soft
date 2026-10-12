'use client';

import React, { useState } from 'react';

// Definición estricta de interfaces en inglés para la cátedra
interface HeaderProps {
    isAuthenticated: boolean;
    activeSensor: 'none' | 'temperature' | 'smoke' | 'trend';
    onLogin: (user: string, password: string) => void;
    onLogout: () => void;
    onSelectSensor: (sensor: 'temperature' | 'smoke' | 'trend') => void;
}
export default function Header({
    isAuthenticated,
    activeSensor,
    onLogin,
    onLogout,
    onSelectSensor,
}: HeaderProps) {
    
    const [user, setUser] = useState('');
    const [password, setPassword] = useState('');
    const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onLogin(user, password);
};
    return (
        <header className="w-full bg-white border-b border-[#e2e8f0]">
            <nav className="w-full grid grid-cols-12 items-center bg-[#f1f5f9]">
            {/* Bloque de Identificación / Logo */}
            <div 
                className="col-span-2 shadow-[2px_2px_5px_rgba(0,0,0,0.2)] text-[1.1em] font-bold bg-gradient-to-b from-[#f8fafc] to-[#cbd5e1] text-center p-2.5" 
                title="Team"
            >
                <span>Design</span> <br />
                <span className="text-[#1e293b]">& &lt;ode</span>
            </div>
            {/* Título Central */}
            <h1 className="col-span-7 text-center text-[1.5em] font-bold text-[#0f172a]" title="Tp Final">
                INGENIERÍA DE SOFTWARE & HARDWARE
            </h1>
            {/* Bloque de Autenticación Condicional */}
            <div className="col-span-3 p-2">
                {!isAuthenticated ? (
                <form onSubmit={handleSubmit} className="grid grid-rows-3 gap-1">
                    <input 
                        className="w-full p-[3px_6px] text-[12px] border border-gray-300 rounded-sm text-black outline-none" 
                        type="text" 
                        placeholder="User" 
                        title="User"
                        value={user}
                        onChange={(e) => setUser(e.target.value)}
                    />
                    <input 
                        className="w-full p-[3px_6px] text-[12px] border border-gray-300 rounded-sm text-black outline-none" 
                        type="password" 
                        placeholder="Password" 
                        title="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <button 
                        type="submit"
                        className="w-full p-[3px_6px] text-[12px] bg-[#0284c7] text-white font-bold rounded-[3px] hover:bg-[#0369a1] transition-colors cursor-pointer" 
                        title="ENTER"
                    >
                        Enter
                    </button>
                </form>
            ) : (
                <div className="h-full flex items-center justify-center text-sm font-semibold text-emerald-600 animate-fade-in">
                    ✓ Logged in as: {user || 'admin'}
                </div>
            )}
            </div>
        </nav>
        {/* RENDERIZADO CONDICIONAL: El menú solo existe si está autenticado */}
        {isAuthenticated && (
            <div className="animate-slide-down">
                <fieldset className="m-[15px] p-[15px] border border-[#cbd5e1] rounded-[6px] grid grid-cols-4 gap-3">
                    <legend className="text-[1.1em] font-bold px-2.5 border border-[#cbd5e1] rounded-[4px] bg-[#f8fafc]">
                        Sensors Menu
                    </legend>
                    <button 
                        onClick={() => onSelectSensor('temperature')}
                        className={`h-[2.5em] font-medium rounded-[4px] transition-all cursor-pointer ${
                            activeSensor === 'temperature' 
                                ? 'bg-sky-100 border-2 border-sky-500 text-sky-700 font-bold' 
                                : 'bg-white border border-[#94a3b8] hover:bg-[#e2e8f0]'
                        }`}
                        >
                            Temperature
                    </button>
                    <button 
                        onClick={() => onSelectSensor('smoke')}
                        className={`h-[2.5em] font-medium rounded-[4px] transition-all cursor-pointer ${
                            activeSensor === 'smoke' 
                                ? 'bg-sky-100 border-2 border-sky-500 text-sky-700 font-bold' 
                                : 'bg-white border border-[#94a3b8] hover:bg-[#e2e8f0]'
                        }`}
                        >
                            Smoke
                    </button>
                    <button 
                        onClick={() => onSelectSensor('trend')}
                        className={`h-[2.5em] font-medium rounded-[4px] transition-all cursor-pointer ${
                            activeSensor === 'trend' 
                            ? 'bg-sky-100 border-2 border-sky-500 text-sky-700 font-bold' 
                            : 'bg-white border border-[#94a3b8] hover:bg-[#e2e8f0]'
                        }`}
                        >
                            Trend Analysis
                    </button>
                    <button 
                        onClick={onLogout}
                        className="h-[2.5em] font-bold bg-[#fecaca] text-[#991b1b] border border-[#f87171] rounded-[4px] hover:bg-[#fca5a5] transition-all cursor-pointer"
                    >
                        Exit
                    </button>
                </fieldset>
            </div>
        )}
        </header>
    );
}