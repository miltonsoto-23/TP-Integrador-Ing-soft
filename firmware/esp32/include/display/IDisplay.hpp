#ifndef I_DISPLAY_HPP
#define I_DISPLAY_HPP
#include <string>
/*#ifndef ARDUINO
    #include <string>
    typedef std::string String;
#else
    #include <Arduino.h>
#endif */

class IDisplay{
    public:
        virtual ~IDisplay(){}
        virtual void init()= 0;
        virtual void printPIN(const std::string buffer) = 0;
        virtual void showStatus(const std::string message, int delayMS = 0) = 0;
        virtual void clearLine(uint8_t row) = 0;
};
#endif // I_DISPLAY_HPP
