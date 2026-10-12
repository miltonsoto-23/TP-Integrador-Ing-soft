#ifndef FAKE_DISPLAY_HPP
#define FAKE_DISPLAY_HPP
#include "IDisplay.hpp"

class FakeDisplay : public IDisplay {
    public:
        std::string lastMessage;
        bool initCall = false;
        std::string pin;
        uint8_t lineClear;
        FakeDisplay(){}
        ~FakeDisplay() override {}
        void showStatus(const std::string message, int delayMs = 0) override {
            lastMessage = message;
        }

        void init() override {
            initCall = true;
        }

        void printPIN(std::string buffer) override {
            pin = buffer;
        }

        void clearLine(uint8_t row) override {
            lineClear = row;
        }
};

#endif // FAKE_DISPLAY_HPP
