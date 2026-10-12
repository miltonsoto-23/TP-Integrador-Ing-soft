#include "access_control/AccessControl.hpp"

AccessControl::AccessControl(IDisplay& disp, IKeypad& kp, network::INetworkClient& net, IActuators& act)
    : display(disp)
    , keypad(kp)
    , network(net)
    , actuators(act)
{
    inputBuffer = "";
    isBlocked = false;
    blockStartTime = 0;
    isShowingTempMessage = false;
    tempMessageStartTime = 0;
}

void AccessControl::init()
{
    display.init();
    display.showStatus("Enter PIN:");
}

char AccessControl::update()
{
    // 1. Clear temporary screen messages after 2 seconds without blocking the CPU
    if (isShowingTempMessage && !isBlocked)
    {
        if (millis() - tempMessageStartTime >= 2000)
        {
            isShowingTempMessage = false;
            display.showStatus("Enter PIN:");
        }
    }

    // 2. Process system lockout UI countdown
    if (isBlocked)
    {
        if (millis() - blockStartTime >= blockDuration)
        {
            isBlocked = false;
            display.showStatus("Enter PIN:");
        }
        else
        {
            unsigned long remainingTime = (blockDuration - (millis() - blockStartTime)) / 1000;
            display.showStatus("LOCKED: " + std::to_string(remainingTime) + "s", 0);
            return '\0'; // Ignore inputs while locked
        }
    }

    // 3. Process hardware inputs
    char key = keypad.getKey();

    if (key)
    {
        if (key == '#')
        {
            display.showStatus("Validating...", 0);

            // Consume API endpoint /api/acceso/validar
            network::AccessResult result = network.postAccessValidation(inputBuffer.c_str());

            if (result.acceso)
            {
                // HTTP 201: Authorized
                display.showStatus("Access Granted", 0);
            }
            else
            {
                if (result.intentosRestantes > 0)
                {
                    display.showStatus("Error (" + std::to_string(result.intentosRestantes) + " left)", 0);
                }
                else
                {
                    isBlocked = true;
                    blockStartTime = millis();
                    display.showStatus("SYSTEM BLOCKED", 0);
                }
            }

            inputBuffer = "";
            isShowingTempMessage = true;
            tempMessageStartTime = millis();
        }
        else if (key == '*')
        {
            inputBuffer = "";
            display.clearLine(1);
        }
        else
        {
            if (inputBuffer.length() < 16)
            {
                inputBuffer += key;
                display.printPIN(inputBuffer.c_str());
            }
        }
    }

    return key ? key : '\0';
}
