#include "access_control/AccessControl.hpp"
#include "actuators/actuators.hpp"
#include "appConfig.hpp"
#include "display/DisplayI2C.hpp"
#include "keypad/MatrixKeypad.hpp"
#include "network/networkClient.hpp"
#include "sensors/EnvironmentSensor.hpp"
#include <Arduino.h>

// 1. Firmware and Network Configuration
app::AppConfig config = {"Personal-576",
                         "00044311319",
                         "https://4d2ed11e-d1c7-4dff-b7ec-854abfd7c4cf.mock.pstmn.io", // Backend URL API Contract
                         "ESP32-ROOM-01"};

/*app::AppConfig config = {"Fede",
                         "designcode",
                         "https://4d2ed11e-d1c7-4dff-b7ec-854abfd7c4cf.mock.pstmn.io", // Backend URL API Contract
                         "ESP32-ROOM-01"}; */

// 2. Hardware Drivers Initialization
network::NetworkClient wifiClient(config);

// Sensors mapped to ADC1 pins (Safe with Wi-Fi)
sensors::EnvironmentSensor roomSensors(34, 35, "LM35", "MQ7");

// Actuators mapped to safe GPIOs, freeing 21 and 22 for I2C
actuators::Actuators roomActuators(25, 26, 27, 32);

DisplayI2C basicDisplay(0x27, 16, 2);
MatrixKeypad hardwareKeypad;

// 3. Core Business Logic Orchestrator
AccessControl accessSystem(basicDisplay, hardwareKeypad, wifiClient, roomActuators);

// Telemetry scheduling variables
unsigned long lastTelemetryTime = 0;
const unsigned long TELEMETRY_INTERVAL = 5000; // 5 seconds

void setup()
{
    Serial.begin(115200);

    roomSensors.begin();
    roomActuators.begin();
    wifiClient.begin();

    Serial.println("\n--- Environment Monitor & Access Control Booting ---");

    accessSystem.init();

    Serial.println("System architecture initialized successfully.");
}

void loop()
{
    // 1. Maintain Infrastructure
    wifiClient.ensureWifiConnection();

    // 2. Process non-blocking state machines
    roomActuators.update();

    char activeKey = accessSystem.update();
    if (activeKey != '\0')
    {
        Serial.print("[Input] Hardware Key Processed: ");
        Serial.println(activeKey);
    }

    // 3. Process Sensor Telemetry (Endpoint 4.2)
    if (millis() - lastTelemetryTime > TELEMETRY_INTERVAL)
    {
        lastTelemetryTime = millis();

        sensors::SensorData payload = roomSensors.read();

        bool isTempCritical = (payload.temperature > 70.0f);
        bool isCOCritical = (payload.percentage > 80.0f);

        // Hardware emergency logic
        if (isTempCritical || isCOCritical)
        {
            roomActuators.setAlarm(true);
            if (isCOCritical)
                roomActuators.setCooler(true);
        }
        else
        {
            roomActuators.setAlarm(false);
            roomActuators.setCooler(false);
        }

        // Dispatch payload to backend
        Serial.println("[Telemetry] Dispatching sensor payload to backend.");
        wifiClient.postSensorReading(payload);
    }
}
