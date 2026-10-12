#ifndef FAKE_ACTUATORS_HPP
#define FAKE_ACTUATORS_HPP

#include "IActuators.hpp"

class FakeActuators : public IActuators {
public:
    bool ultimoEstadoLed = false;
    bool metodoLlamado = false;

    void setAccessStatusLED(bool authorized) override {
        ultimoEstadoLed = authorized;
        metodoLlamado = true;
    }
};

#endif // FAKE_ACTUATORS_HPP
