``` mermaid
graph TD

    %% Estilos
    classDef micro fill:#2d3436,stroke:#00b894,stroke-width:3px,color:#fff;
    classDef sensor fill:#0984e3,stroke:#74b9ff,stroke-width:2px,color:#fff;
    classDef output fill:#d63031,stroke:#ff7675,stroke-width:2px,color:#fff;
    classDef cloud fill:#6c5ce7,stroke:#a29bfe,stroke-width:2px,color:#fff;

    %% Nodo Central
    ESP32[" ESP32 (Microcontrolador)"]:::micro

    %% Entradas (Sensores)
    LM35[" Sensor LM35 (Temp)"]:::sensor
    MQ7[" Sensor MQ7 (Gas/CO)"]:::sensor
    Keypad["⌨ Teclado Matricial 4x4"]:::sensor

    %% Salidas (Actuadores y UI)
    LCD[" Pantalla LCD 16x2 (I2C)"]:::output
    Actuadores["LEDS indicadores"]:::output
    
    SubComponentes["🟢 LED Verde (Acceso)<br>🔴 LED Rojo & Buzzer (Alarma)<br>🟡 LED Amarillo (Ventilación)"]

    %% Nube / Red
    Backend[" Backend Spring Boot (API REST)"]:::cloud

    %% Conexiones de Entrada
    LM35 -- "Señal Analógica (GPIO 34)" --> ESP32
    MQ7 -- "Señal Analógica (GPIO 35)" --> ESP32
    Keypad -- "Pines Digitales" --> ESP32

    %% Conexiones de Salida
    ESP32 -- "SDA (21) / SCL (22)" --> LCD
    ESP32 -- "GPIO (25, 26, 27, 32)" --> Actuadores
    Actuadores -. "Corte/Saturación" .-> SubComponentes

    %% Conexión de Red
    ESP32 -- "Wi-Fi / JSON payload" --> Backend
    
    ```