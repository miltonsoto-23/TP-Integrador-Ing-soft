#ifndef ACCESS_CONTROL_HPP
#define ACCESS_CONTROL_HPP

#include <Arduino.h>
#include "display/IDisplay.hpp"
#include "keypad/IKeypad.hpp"
#include "network/INetworkClient.hpp"
#include "actuators/IActuators.hpp"

class AccessControl {
private:
    // Dependency Injection
    IDisplay& display;
    IKeypad& keypad;
    network::INetworkClient& network;
    IActuators& actuators;

    String inputBuffer;

    // Local lockout UI state tracking
    bool isBlocked;
    unsigned long blockStartTime;
    const unsigned long blockDuration = 10000; // 10s local UI block

    // UI Message state tracking
    bool isShowingTempMessage;
    unsigned long tempMessageStartTime;

public:
    AccessControl(IDisplay& disp, IKeypad& kp, network::INetworkClient& net, IActuators& act);

    void init();
    char update();
};

#endif // ACCESS_CONTROL_HPP
