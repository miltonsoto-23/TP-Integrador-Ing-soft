#include "sensors/EnvironmentSensor.hpp"

namespace sensors
{
    EnvironmentSensor::EnvironmentSensor(uint8_t lm35Pin,
                                         uint8_t mq7Pin,
                                         const String& sensor1Id,
                                         const String& sensor2Id)
        : lm35Pin_(lm35Pin)
        , mq7Pin_(mq7Pin)
        , sensor1Id_(sensor1Id)
        , sensor2Id_(sensor2Id)
    {
        // Constructor initializes the pin member variables
    }

    void EnvironmentSensor::begin()
    {
        // Configure ESP32 ADC resolution to 12 bits (0-4095)
        analogReadResolution(12);
        Serial.println("[EnvironmentSensor] Initialized successfully.");
    }

    float EnvironmentSensor::readTemperature() const
    {
        int rawValue = analogRead(lm35Pin_);

        // Convert raw ADC value to voltage (ESP32 max voltage is 3.3V)
        float voltage = (rawValue / 4095.0f) * 3300.0f;

        // LM35 outputs 10mV per degree Celsius
        float temperature = voltage / 10.0f;

        return temperature;
    }

    float EnvironmentSensor::readCOLevel() const
    {
        int rawValue = analogRead(mq7Pin_);

        // Map raw value to a generic percentage for telemetry
        float percentage = (rawValue / 4095.0f) * 100.0f;

        return percentage;
    }

    SensorData EnvironmentSensor::read() const
    {
        SensorData data;
        data.sensor1Id = sensor1Id_;
        data.sensor2Id = sensor2Id_;
        data.temperature = readTemperature();
        data.percentage = readCOLevel();
        return data;
    }
} // namespace sensors
