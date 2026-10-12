# API Contract -- IoT sistema de monitoreo

## 1. Ques es una API ?
Una API(Application Programming Interface) es una interfaz que actua como intermediario entre dos aplicaciones, permitiendo la comunicacion entre ellos sin necesidad de conocer como esta implementada cada una.

En este sistema las dos partes son:<br>
- Cliente: ESP32 (firmware) y PaginaWeb (frontend)
- Servidor: Backend

Los datos viajan empaquetados en formato JSON (JavaScript Object Notation), formato de texto estructurado.<br>
Cualquier cambio en la estructura de los JSON aquí definidos debe ser acordado por el equipo completo, ya que impacta directamente en las 3 capas del sistema.

---
## 2. Protocolo HTTP
La comunicacion se realiza sobre el protocolo HTTP(HyperText Transfer Protocol), que define como se estructuran y transmiten los mensajes entre cliente y servidor.<br>

Los metodos tipicos de HTTP son:<br>
| Metodo | Proposito |
| ------ | --------- |
| GET    | Solicita datos |
| POST   | Envia datos nuevos |
| PUT    | Modifica datos existentes |
| DELETE | Elimina datos |

En este caso solo se utilizan **GET** y **POST**.

---
## 3. Estilo REST
Esta API sigue el estilo arquitectonico de software REST(Representational State Transfer).<br>
En REST cada endpoint es la combinacion de:<br>
- Un metodo HTTP(GET, POST)
- Una URL que identifica el recurso<br>
Ejemplo: <br>
POST /api/telemetria <br>
GET /api/historial

---
## 4. Endpoints

### 4.1 Validación de Acceso (Teclado Matricial)
Este endpoint es consumido por la placa ESP32 para verificar si el PIN ingresado por el usuario en el teclado físico es válido para desbloquear el sistema.

* **Ruta:** `/api/acceso/validar`
* **Metodo:** POST
* **Consumidor:** Firmware (ESP32)

#### Request (Entrada) [Payload]
```json
{
  "pinIngresado": "1234"
}
```

#### Responses (Salida)

| Code | Meaning |
| ---- | ------- |
| 201  | PIN correct, access granted |
| 401  | PIN incorrect, access denied |
| 400  | Invalid payload |
| 423  | System locked after 3 failed attempts |
| 500  | Internal server error |

🟢 201 Created (Datos guardados)<br>
```json
{
  "mensaje": "Acceso concedido.",
  "acceso": true,
  "timestamp": "2026-05-31T20:00:00Z",
  "error":"null",
  "intentosRestantes":"0",
  "bloqueadoHasta":"null"
}
```
<br>🔴 400 Bad Request (Payload invalido)
```json
{
  "mensaje": "null",
  "acceso": false,
  "timestamp": "",
  "error": "El campo 'pinIngresado' es obligatorio.",
  "intentosRestantes":"0",
  "bloqueadoHasta":"null"
}
```
<br>🔴 401 Not authorized (PIN incorrecto)
```json
{
  "mensaje": "null",
  "acceso": false,
  "timestamp": "",
  "error": "PIN incorrecto, Acceso denegado.",
  "intentosRestantes": 2
  "bloqueadoHasta":"null"
}
```
<br>🔴 423 Locked (sistema bloqueado por intentos fallidos)
```json
{
  "mensaje": "null",
  "acceso": false,
  "timestamp": "",
  "error": "Sistema bloqueado "
  "intentosRestantes": 0,
  "bloqueadoHasta": "2026-05-31T20:10:00Z"
}
```
<br>🔴 500 Internal Server Error
```json
{
  "mensaje": "null",
  "acceso": false,
  "timestamp": "null",
  "error": "Error interno del servidor, intente nuevamente."
  "intentosRestantes": 0,
  "bloqueadoHasta": "null"
}
```

---
### 4.2 Envio de datos
Este endpoint es consumido por la ESP32, es el proceso de envio de datos cada cierto tiempo.

* **Ruta:** /api/envio
* **Metodo:** POST
* **Consumidor:** ESP32
* **Parámetros de URL (Query Params):** Ninguno
* **Ejemplo de petición:** POST /api/envio

#### Request (Entrada) [Payload]
```json
{
  "sensor1Id":"lm35",
  "sensor2Id":"mq7",
  "temperature":"24.5",
  "smoke":"55.22"
}
```

#### Responde (salida)
🟢 200 OK (Datos recibidos)
```json
{
  "mensaje": "Telemetria registrada correctamente"
}
```
<br>🔴 500 Internal Server Error (Fallo al guardar en BDD)
```json
{
  "error": "Error interno del servidor, no se pudo registrar la telemetria."
}
```

---
### 4.3 Historial de Telemetría (Gráficos Dashboard)
Este endpoint es consumido por la aplicación web en React para obtener la serie temporal de datos y dibujar las curvas en los gráficos.

* **Ruta:** /api/historial
* **Metodo:** GET
* **Consumidor:** Frontend (React)
* **Parámetros de URL (Query Params):** * sensor (String, Obligatorio): Indica qué métrica traer (temperatura o humo).
* **Ejemplo de petición:** GET /api/historial?sensor=temperatura&horas=12

#### Response (Salida)

| Code | Meaning |
| ---- | ------- |
| 200  | Interval reading returned |
| 404  | No readings available yet |

#### Request (Entrada) [Payload]
```json
{
  "sensores":"smoke",
}
```

🟢 200 OK
```json
{
  "sensor": "smoke",
  "datos": [
    { "tiempo": "2026-05-31T20:00:00Z", "valor": 23.5 },
    { "tiempo": "2026-05-31T21:00:00Z", "valor": 24.1 },
    { "tiempo": "2026-05-31T22:00:00Z", "valor": 25.0 }
  ]
}
```

🔴 404 Bad Request (No readings available yet)
```json
{
  "error": "Error no hay lecturas disponibles"
}
```
---

### 4.4 Motor de Estadísticas (Patrón Strategy)

Este endpoint expone los cálculos matemáticos procesados dinámicamente por el backend sobre el histórico de datos.

* **Ruta:** /api/estadistica_sensor
* **Metodo:** GET
* **Consumidor:** Frontend (React)
* **Parámetros de URL (Query Params):**
* **sensor (String, Obligatorio):** temperatura o humo
* **operacion (String, Obligatorio):** Tipo de estrategia a aplicar (media, mediana)
* **Ejemplo de petición:** GET /api/estadistica_sensor=humo&operacion=mediana

#### Request (Entrada) [Payload]
```json
{
  "estrategia":"media",
  "sensor":"temperatura"
}
```

#### Responses (Salida)
🟢 200 OK (Cálculo exitoso)
```json
{
  "sensor": "smoke",
  "valor": 315.5
}
```

🔴 400 Bad Request (Estrategia inválida)
```json
{
  "error": "Estrategia no soportada. Operaciones válidas: [media, mediana]"
}
```
### 4.5 Inicio de Sesion

Este endpoint es consumido por la pagina web. Para autenticar el usuario y password.

* **Ruta:** /api/autenticar
* **Metodo:** POST
* **Consumidor:** Frontend (React)
* **Parámetros de URL (Query Params):** Ninguno
* **Ejemplo de petición:** POST /api/autenticar

#### Request (Entrada) [Payload]
```json
{
  "username": "tu_usuario",
  "password": "tu_contraseña_secreta"
}
```

#### Responses (salida)
🟢 200 OK (Inicio exitoso)
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "username": "tu_usuario",
    "role": "admin"
  }
}
```

🔴 400 Bad Request (Internal server error)
```json
{
  "error": "Error interno al procesar el estado"
}
```

🔴 401 Usuario o contraseña Incorrecta (Internal server error)
```json
{
  "error": "Usuario o contraseña incorrecta, vuelva a intentar"
}
```
