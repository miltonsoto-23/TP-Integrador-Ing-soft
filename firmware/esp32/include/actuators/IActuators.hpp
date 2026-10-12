#ifndef I_ACTUATORS_HPP
#define I_ACTUATORS_HPP

class IActuators {
public:
    virtual ~IActuators() {}
    virtual void setAccessStatusLED(bool authorized) = 0;
};

#endif // I_ACTUATORS_HPP
