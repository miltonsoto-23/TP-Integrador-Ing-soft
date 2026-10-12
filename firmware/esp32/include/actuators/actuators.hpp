#ifndef FIRMWARE_ESP32_INCLUDE_ACTUATORS_ACTUATORS_HPP
#define FIRMWARE_ESP32_INCLUDE_ACTUATORS_ACTUATORS_HPP

#include <Arduino.h>
#include "IActuators.hpp"

namespace actuators {

    /**
     * @brief Controls the physical outputs (LEDs, Buzzer, Cooler) using non-blocking logic.
     */
    class Actuators : public IActuators{
    public:
        Actuators(uint8_t greenLedPin, uint8_t redLedPin, uint8_t buzzerPin, uint8_t coolerPin);

        void begin();
        void setCooler(bool turnOn);
        void setAlarm(bool turnOn);
        void setAccessStatusLED(bool authorized) override;

        /**
         * @brief Non-blocking state machine update. Must be called in the main loop.
         */
        void update();

    private:
        uint8_t greenLedPin_;
        uint8_t redLedPin_;
        uint8_t buzzerPin_;
        uint8_t coolerPin_;

        bool isAlarmActive_;

        // Non-blocking timer variables
        unsigned long accessTimerStart_;
        bool isDisplayingAccess_;
        uint8_t activeAccessPin_;
    };

} // namespace actuators

#endif // FIRMWARE_ESP32_INCLUDE_ACTUATORS_ACTUATORS_HPP
