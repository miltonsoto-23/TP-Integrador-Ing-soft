#ifndef DISPLAY_I2C_HPP
#define DISPLAY_I2C_HPP

#include <Arduino.h>
#include <LiquidCrystal_I2C.h>
#include "IDisplay.hpp"

class DisplayI2C : public IDisplay
{
private:
    LiquidCrystal_I2C lcd;

public:
    // Constructor configures target address and geometry
    DisplayI2C(uint8_t lcd_addr, uint8_t lcd_cols, uint8_t lcd_rows);

    void init() override;
    void printPIN(std::string buffer) override;
    void showStatus(const std::string message, int delayMs = 0) override;
    void clearLine(uint8_t row) override;
};

#endif // DISPLAY_I2C_HPP
