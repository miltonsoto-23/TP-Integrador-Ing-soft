```mermaid
classDiagram
    %% ==========================================
    %% ENTIDADES Y DTOs (Data Transfer Objects)
    %% ==========================================
    class RegistroAmbiental {
        <<Entity>>
        - temp : double
        - gas : double
        - timestamp : Date
    }

    class RegistroAcceso {
        <<Entity>>
        - pin : String
        - exitoso : boolean
        - timestamp : Date
    }

    class LoginRequestDTO {
        <<DTO>>
        - usuario : String
        - password : String
    }

    class TelemetryResponseDTO {
        <<DTO>>
        - valorCalculado : Double
        - historialDatos : List~Double~
    }

    %% ==========================================
    %% CONTROLADORES (Endpoints)
    %% ==========================================
    class IotController {
        <<Controller>>
        + recibirTelemetria(payload : Json) : Response
        + recibirIntentoAcceso(payload : Json) : Response
    }

    class WebController {
        <<Controller>>
        + getHistorial(tipoSensor : String) : Json
        + getEstadistica(tipoSensor : String, tipoCalculo : String) : Json
        + getEstadoHabitacion() : Json
    }

    class AuthController {
        <<Controller>>
        + login(payload : LoginRequestDTO) : Json
    }

    %% ==========================================
    %% PATRÓN SINGLETON (Base de Datos)
    %% ==========================================
    class DatabaseConnection {
        <<Singleton>>
        - instance : DatabaseConnection$
        - DatabaseConnection()
        + getInstance() : DatabaseConnection$
        + executeQuery(query : String) : Result
    }

    %% ==========================================
    %% PATRÓN STRATEGY (Estadísticas)
    %% ==========================================
    class CalculationStrategy {
        <<Interface>>
        + calculate(values : List~Double~) : Double
    }

    class MeanStrategy {
        + calculate(values : List~Double~) : Double
    }

    class MedianStrategy {
        + calculate(values : List~Double~) : Double
    }

    class StatisticsService {
        <<Service>>
        - strategy : CalculationStrategy
        + setStrategy(strategy : CalculationStrategy) : void
        + processData(values : List~Double~) : Double
    }

    %% ==========================================
    %% PATRÓN OBSERVER (Alertas y Logs)
    %% ==========================================
    class TelemetryObserver {
        <<Interface>>
        + update(data : Object) : void
    }

    class AlertaWebsocketObserver {
        + update(data : Object) : void
        - emitirWebsocket() : void
    }

    class AuditoriaAccesosObserver {
        + update(data : Object) : void
        - registrarIntento() : void
    }

    class MonitoreoService {
        <<Service / Subject>>
        - observers : List~TelemetryObserver~
        + addObserver(obs : TelemetryObserver) : void
        + procesarAmbiental(temp : double, humo : double) : void
    }

    class AccesoService {
        <<Service / Subject>>
        - observers : List~TelemetryObserver~
        + addObserver(obs : TelemetryObserver) : void
        + validarPin(pin : String) : boolean
    }

    %% ==========================================
    %% PATRÓN STATE (Estado de la Habitación)
    %% ==========================================
    class EstadoHabitacion {
        <<Interface>>
        + evaluarSeguridad(temp : double, gas : double) : void
        + procesarAcceso(pin : String) : void
    }

    class EstadoNormal {
        + evaluarSeguridad(temp : double, gas : double) : void
        + procesarAcceso(pin : String) : void
    }

    class EstadoAlertaAmbiental {
        + evaluarSeguridad(temp : double, gas : double) : void
        + procesarAcceso(pin : String) : void
    }

    class EstadoAccesoConcedido {
        + evaluarSeguridad(temp : double, gas : double) : void
        + procesarAcceso(pin : String) : void
    }

    class HabitacionContext {
        - estadoActual : EstadoHabitacion
        + setEstado(estado : EstadoHabitacion) : void
        + requestAcceso(pin : String) : void
        + triggerEmergencia() : void
        + getEstado() : String
    }

    %% ==========================================
    %% RELACIONES
    %% ==========================================
    
    %% Flujo de Controladores a Servicios y DTOs
    IotController ..> MonitoreoService : usa
    IotController ..> AccesoService : usa
    
    WebController ..> StatisticsService : usa
    WebController ..> HabitacionContext : consulta estado
    WebController ..> TelemetryResponseDTO : devuelve
    WebController ..> DatabaseConnection : extrae lista
    
    AuthController ..> LoginRequestDTO : recibe
    AuthController ..> DatabaseConnection : valida credenciales

    %% Relaciones Singleton
    MonitoreoService ..> DatabaseConnection : persiste
    AccesoService ..> DatabaseConnection : persiste
    StatisticsService ..> DatabaseConnection : consulta
    
    %% Relaciones Strategy
    StatisticsService "1" *-- "1" CalculationStrategy : delega cálculo
    CalculationStrategy <|.. MeanStrategy : implementa
    CalculationStrategy <|.. MedianStrategy : implementa

    %% Relaciones Observer
    MonitoreoService "1" --> "*" TelemetryObserver : notifica
    AccesoService "1" --> "*" TelemetryObserver : notifica
    TelemetryObserver <|.. AlertaWebsocketObserver : implementa
    TelemetryObserver <|.. AuditoriaAccesosObserver : implementa

    %% Relaciones State
    HabitacionContext "1" *-- "1" EstadoHabitacion : mantiene
    EstadoHabitacion <|.. EstadoNormal : implementa
    EstadoHabitacion <|.. EstadoAlertaAmbiental : implementa
    EstadoHabitacion <|.. EstadoAccesoConcedido : implementa
    ```