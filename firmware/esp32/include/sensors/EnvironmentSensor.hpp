#ifndef FIRMWARE_ESP32_INCLUDE_SENSORS_ENVIRONMENT_SENSOR_HPP
#define FIRMWARE_ESP32_INCLUDE_SENSORS_ENVIRONMENT_SENSOR_HPP

#include <Arduino.h>

namespace sensors
{
    /**
     * @brief holds a  complete sensor reading snapshot.
     */
    struct SensorData
    {
        String sensor1Id;
        String sensor2Id;
        float temperature;
        float percentage;
    };

    /**
     * @brief Handles the environmental monitoring (Temperature and CO levels).
     */
    class EnvironmentSensor
    {
    public:
        /**
         * @brief Constructor that assigns the hardware pins.
         * * @param lm35Pin Analog pin connected to the LM35 temperature sensor.
         * @param mq7Pin Analog pin connected to the MQ7 CO sensor.
         */
        EnvironmentSensor(uint8_t lm35Pin, uint8_t mq7Pin, const String& sensor1Id, const String& sensor2Id);

        /**
         * @brief Configures the ADC resolution and initializes the module.
         */
        void begin();

        /**
         * @brief Reads and calculates the current temperature in Celsius.
         * * @return float Temperature in °C.
         */
        float readTemperature() const;

        /**
         * @brief Reads and calculates the Carbon Monoxide level percentage.
         * * @return float CO level (0.0 to 100.0%).
         */
        float readCOLevel() const;

        /**
         * @brief read and fill in the data of the structure
         * * @return data (name of sensors and values)
         */
        SensorData read() const;

    private:
        uint8_t lm35Pin_;
        uint8_t mq7Pin_;
        String sensor1Id_;
        String sensor2Id_;
    };

} // namespace sensors

#endif // FIRMWARE_ESP32_INCLUDE_SENSORS_ENVIRONMENT_SENSOR_HPP
