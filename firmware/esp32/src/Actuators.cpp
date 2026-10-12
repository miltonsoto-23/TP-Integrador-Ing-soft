#include "actuators/actuators.hpp"

namespace actuators
{

    Actuators::Actuators(uint8_t greenLedPin, uint8_t redLedPin, uint8_t buzzerPin, uint8_t coolerPin)
        : greenLedPin_(greenLedPin)
        , redLedPin_(redLedPin)
        , buzzerPin_(buzzerPin)
        , coolerPin_(coolerPin)
        , isAlarmActive_(false)
    {
    }

    void Actuators::begin()
    {
        pinMode(greenLedPin_, OUTPUT);
        pinMode(redLedPin_, OUTPUT);
        pinMode(buzzerPin_, OUTPUT);
        pinMode(coolerPin_, OUTPUT);

        // Default state: Optimal (Green ON, others OFF)
        digitalWrite(greenLedPin_, HIGH);
        digitalWrite(redLedPin_, LOW);
        digitalWrite(buzzerPin_, LOW);
        digitalWrite(coolerPin_, LOW);

        Serial.println("[Actuators] Initialized successfully. Environment mode strictly active.");
    }

    void Actuators::setCooler(bool turnOn)
    {
        // Cooler acts as the mitigation actuator (Yellow LED / Fan)
        digitalWrite(coolerPin_, turnOn ? HIGH : LOW);

        // Only update Green LED if there is no critical alarm overriding the visual state
        if (!isAlarmActive_)
        {
            digitalWrite(greenLedPin_, turnOn ? LOW : HIGH);
            digitalWrite(redLedPin_, LOW);
            digitalWrite(buzzerPin_, LOW);
        }
    }

    void Actuators::setAlarm(bool turnOn)
    {
        isAlarmActive_ = turnOn;

        if (turnOn)
        {
            // CRITICAL ALARM: Red ON, Buzzer ON, Green OFF
            digitalWrite(buzzerPin_, HIGH);
            digitalWrite(redLedPin_, HIGH);
            digitalWrite(greenLedPin_, LOW);
        }
        else
        {
            // ALARM OFF: Silence buzzer and turn off Red LED
            digitalWrite(buzzerPin_, LOW);
            digitalWrite(redLedPin_, LOW);

            // Check if mitigation (Cooler/Yellow) is still running to decide Green LED state
            bool isMitigating = digitalRead(coolerPin_);
            digitalWrite(greenLedPin_, isMitigating ? LOW : HIGH);
        }
    }

    void Actuators::setAccessStatusLED(bool authorized)
    {
        // DEPRECATED: Intentionally left empty to prevent linker errors
        // if this is still declared in actuators.hpp
    }

    void Actuators::update()
    {
        // DEPRECATED: Intentionally left empty. LEDs are now strictly real-time.
    }

} // namespace actuators
