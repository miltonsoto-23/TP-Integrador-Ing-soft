graph TD
    %% Estilos de los nodos
    classDef backend fill:#f1f5f9,stroke:#64748b,stroke-width:2px,color:#334155;
    classDef views fill:#e0f2fe,stroke:#0284c7,stroke-width:2px,color:#0369a1;
    classDef components fill:#f0fdf4,stroke:#16a34a,stroke-width:2px,color:#14532d;
    classDef store fill:#faf5ff,stroke:#7e22ce,stroke-width:2px,color:#581c87;

    %% Bloque Backend
    B[BACKEND]:::backend

    %% Vistas Principales
    L[Logg-In View]:::views
    D[DASHBOARD VIEW <br> Data Manager / Utils]:::views

    %% Componentes Visuales
    MC[Metric Cards <br> Metric Table]:::components
    TC[Trend Chart <br> Graphic Lines]:::components
    SD[Statistical Data <br> Show Data]:::components

    %% Estado Global
    Z[ZUSTAND STORE <br> Global State <br> - Alertas en tiempo real Humo/Temp <br> - Token de Sesión Activa]:::store

    %% Conexiones e Interacciones
    L <-->|1. Envía user/pass <br> 2. Devuelve Token| B
    L -->|Si el token es OK <br> Patrón Lollipop| D
    L -->|Gatilla alerta de error <br> o guarda Token| Z

    B ==>|3. Responde Datos Crudos <br> Polling cada 2 segundos| D

    %% Distribución de data limpia hacia abajo
    D -->|Envía data limpia| MC
    D -->|Envía data limpia| TC
    D -->|Envía data limpia| SD

    %% Flujo de Alertas de Sensores
    MC -->|Si los valores son críticos| Z
    Z -.->|Escucha estado global| TC