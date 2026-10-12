```mermaid
classDiagram
    %% ==========================================
    %% ENTIDADES Y DTOs (Data Transfer Objects)
    %% ==========================================
    class SensorReading {
        <<Entity>>
        - Id : Integer
        - temperature : float
        - percentage : float
        - createdAt : Instant
    }

    class AccessAttempt {
        <<Entity>>
        - Id : Integer
        - pin : String
        - exitoso : boolean
        - timestamp : Instant
    }

    class User{
        <<Entity>>
        - Id : Integer
        - username : String
        - password : String
        - role : String 

    }

    class AccessResponseDto {
        - Id : Integer
        - exitoso : boolean
        - timestamp : Instant
        + fromEntity(AccessReading entity) : AccessResponseDto
    }

    class CreateAccessDto {
        - pin : String           
    }

    class CreateSensorDto {
        - temperature : float
        - percentage : float
    }

    class LoginRequestDto {
        - email : String
        - password : String
    }

    class LoginResponseDto {
        - success: Boolean
        - message : String
        - email: String
        - role : String 
    }
    
    class SensorResponseDto {
        - Id : Integer
        - temperature : float
        - percentage : float
        - createdAt : Instant
    }

    class TelemetryResponseDto {
    - tipoSensor : String
    - valores : List~Double~
    - calculoResultado : Double
    }
    
    %% ==========================================
    %% CONTROLADORES (Endpoints)
    %% ==========================================
    class AccessController {
        <<Controller>>
        -AccessService accessService
        +AccessController(AccessService accessService)
        +processAttempt(CreateAccessDto dto) ResponseEntity~AccessResponseDto~
        +listLogs() List~AccessResponseDto~
    }

    class SensorsController {
        <<Controller>>
        -SensorsService sensorsService
        +SensorsController(SensorsService sensorsService)
        +create(CreateSensorDto dto) ResponseEntity~SensorResponseDto~
        +list() List~SensorResponseDto~
    }

    class AuthController {
        <<Controller>>
        -AuthService authService
        +login(LoginRequestDto loginDto) ResponseEntity~LoginResponseDto~
    }

    class WebController {
        <<Controller>>
        - StatisticsService statisticsService
        - SensorsService sensorsService
        + getHistorial(tipoSensor : String) Json
        + getEstadistica(tipoSensor : String, tipoCalculo : String) Json
    }
    class WebConfig{
        +addCorsMappings(CorsRegistry registry) void
    }
    %% ==========================================
    %% Services
    %% ==========================================
    class AuthService{
        +credentialValidation(String email, String password) Optional~User~
    }
    %% ==========================================
    %% Repositories
    %% ==========================================
    class AccessRepository {
        <<interface>>
    }
    class SensorsRepository {
        <<interface>>
    }
    class UserRepository {
        <<interface>>
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

    class WebsocketAlertObserver {
        + update(data : Object) : void
        - emitirWebsocket() : void
    }

    class AccessAuditObserver {
        + update(data : Object) : void
        - registrarIntento() : void
    }

    class SensorsService {
        <<Service >>
        -SensorsRepository sensorsRepository
        -SensorsMonitor sensorsMonitor
        +SensorsService(SensorsRepository repository, SensorsMonitor monitor)
        +create(CreateSensorDto dto) SensorResponseDto
        +findAll() List~SensorResponseDto~
    }
    
    class SensorsMonitor {
        <<Subject>>
        - observers : List~TelemetryObserver~
        + addObserver(obs : TelemetryObserver) : void
        + procesarAmbiental(temp : double, humo : double) : void
    }

    class AccessService {
        <<Service>>
        -AccessService accessService
        +AccessController(AccessService accessService)
        +processAttempt(CreateAccessDto dto) ResponseEntity~AccessResponseDto~
        +listLogs() List~AccessResponseDto~
    }

   class AccessMonitor {
        <<Subject>>
        - observers : List~TelemetryObserver~
        + addObserver(obs : TelemetryObserver) : void
        + validarPin(pin : String) : boolean
    }
    %% ==========================================
    %% PATRÓN CHAIN OF RESPONSIBILITY
    %% ==========================================
   
   class Handler {
        <<interface>>
        +setNext(Handler handler) void
        +handle(AccessContext context) boolean
    }

   class BaseHandler {
        <<abstract>>
        -Handler next
        +setNext(Handler handler) void
        +handle(AccessContext context) boolean
    }

   class PinFormatHandler {
        +handle(AccessContext context) boolean
    }

   class PinVerificationHandler {
        -SystemConfigRepository configRepository
        -int remainingAttempts
        +PinVerificationHandler(SystemConfigRepository configRepository)
        +handle(AccessContext context) boolean
    }

   class ChainConfig {
        +validationChain(SystemConfigRepository configRepository) Handler
    }

   class AccessContext {
        -String pin
        -boolean successful
        -String errorMessage
        -Integer remainingAttempts
        -Instant bloqueadoHasta
        -int httpStatusCode
        +AccessContext(String pin)
    }

    %% ==========================================
    %% RELACIONES
    %% ==========================================
    
    %% Flujo de Controladores a Servicios y DTOs

    
    WebController ..> StatisticsService : usa
    WebController ..> TelemetryResponseDto : devuelve
    WebController --> StatisticsService : Inyecta / Delega análisis
    WebController --> SensorsService : Inyecta / Consulta datos base   
    
    AuthController ..> LoginRequestDto : Recibe como parámetro
    AuthController --> AuthService : Inyecta / Delega
    AuthController ..> LoginResponseDto : Instancia y retorna
    AuthController ..> User : Desempaqueta y lee datos
    AuthService ..> User : Busca y retorna (envuelto en Optional)
    AuthService --> UserRepository : Inyecta / Consulta
    UserRepository ..> User : Busca y envuelve

    AccessController --> AccessService : Inyecta / Delega
    AccessController ..> CreateAccessDto : Usa como parámetro
    AccessController ..> AccessResponseDto : Retorna
    AccessService ..> AccessResponseDto : Retorna  
    AccessService ..> AccessAttempt : Construye y manipula        
    AccessService --> AccessRepository : Inyecta / Persiste
    AccessService --> AccessMonitor : Inyecta / Notifica



    SensorsController --> SensorsService : Inyecta / Delega
    SensorsController ..> SensorResponseDto : Retorna
    SensorsController ..> CreateSensorDto : Usa como parámetro   
    SensorsService ..> CreateSensorDto : Recibe como parámetro    
    SensorsService ..> SensorReading : Construye y manipula
    SensorsService ..> SensorResponseDto : Retorna    
    SensorsService --> SensorsRepository : Inyecta / Usa
    SensorsService --> SensorsMonitor : Inyecta / Notifica
    SensorResponseDto ..> SensorReading : Se mapea desde (fromEntity)
    AccessRepository ..> AccessAttempt : Guarda / Recupera
    AccessMonitor ..> AccessAttempt : Recibe para notificar
    AccessResponseDto ..> AccessAttempt : Se mapea desde (fromEntity)    
    
    %% Relaciones Strategy
    StatisticsService "1" *-- "1" CalculationStrategy : delega cálculo
    CalculationStrategy <|.. MeanStrategy : implementa
    CalculationStrategy <|.. MedianStrategy : implementa

    %% Relaciones Observer
    SensorsMonitor "1" --> "*" TelemetryObserver~SensorReading~ : Mantiene / Notifica
    SensorsMonitor ..> SensorReading : Pasa a los observadores
    AccesesMonitor "1" --> "*" TelemetryObserver : notifica
    TelemetryObserver <|.. WebsocketAlertObserver : implementa
    TelemetryObserver <|.. AccessAuditObserver : implementa
    TelemetryObserver ..> SensorReading : Recibe en el método update
    %%Relaciones Chain of Responsibility 
    BaseHandler --> Handler : next (composición/enlace)
    ChainConfig ..> Handler : Crea y configura la cadena
    ChainConfig ..> PinFormatHandler : Hace "new"
    ChainConfig ..> PinVerificationHandler : Hace "new"  
    Handler ..> AccessContext : Procesa a través de los métodos handle()
    Handler <|.. BaseHandler : Implementa
    BaseHandler <|-- PinFormatHandler : Hereda de
    BaseHandler <|-- PinVerificationHandler : Hereda de
    AccessService --> Handler : validationChain (Usa la abstracción)
    AccessService ..> AccessContext : Crea e inspecciona resultado
    ``` 

