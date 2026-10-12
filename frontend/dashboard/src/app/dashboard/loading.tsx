export default function DashboardLoading() {
  return (
    <div className="w-full min-h-screen p-6 flex flex-col gap-6 bg-[#f8fafc]">
      {/* Skeleton del Título de la Sección */}
      <div className="h-8 bg-gray-200 rounded-md w-1/4 animate-pulse" />
      {/* Grilla principal simulando el diseño simétrico del Dashboard */}
      <div className="w-full grid grid-cols-1 lg:grid-cols-12 gap-6 flex-grow">
        
        {/* Columna Izquierda: Simula la DataTable (Ocupa 5 columnas de 12) */}
        <div className="lg:col-span-5 bg-white p-6 border border-gray-200 rounded-lg shadow-sm flex flex-col gap-4 animate-pulse">
          <div className="h-6 bg-gray-200 rounded w-1/3 mb-2" />
          <div className="h-10 bg-gray-100 rounded w-full" />
          <div className="h-10 bg-gray-100 rounded w-full" />
          <div className="h-10 bg-gray-100 rounded w-full" />
          <div className="h-10 bg-gray-100 rounded w-full" />
          <div className="h-10 bg-gray-100 rounded w-full" />
        </div>
        {/* Columna Derecha: Simula los Gráficos y Estadísticas (Ocupa 7 columnas de 12) */}
        <div className="lg:col-span-7 flex flex-col gap-6">
          {/* Bloque superior: Simula el espacio del Gráfico Activo */}
          <div className="bg-white p-6 border border-gray-200 rounded-lg shadow-sm h-[320px] flex flex-col justify-between animate-pulse">
            <div className="h-6 bg-gray-200 rounded w-1/4" />
            <div className="w-full h-[200px] bg-gray-100 rounded-md" />
          </div>
          {/* Bloque inferior: Simula las Cards de Estadísticas / Alertas */}
          <div className="grid grid-cols-2 gap-4">
            <div className="bg-white p-4 border border-gray-200 rounded-lg shadow-sm h-28 flex flex-col justify-between animate-pulse">
              <div className="h-4 bg-gray-200 rounded w-1/2" />
              <div className="h-8 bg-gray-300 rounded w-1/3" />
            </div>
            <div className="bg-white p-4 border border-gray-200 rounded-lg shadow-sm h-28 flex flex-col justify-between animate-pulse">
              <div className="h-4 bg-gray-200 rounded w-1/2" />
              <div className="h-8 bg-gray-300 rounded w-1/3" />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}