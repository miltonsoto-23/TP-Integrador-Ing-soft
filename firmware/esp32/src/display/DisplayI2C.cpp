#include "display/DisplayI2C.hpp"

DisplayI2C::DisplayI2C(uint8_t lcd_addr, uint8_t lcd_cols, uint8_t lcd_rows)
    : lcd(lcd_addr, lcd_cols, lcd_rows)
{
}

void DisplayI2C::init()
{
    lcd.init();
    lcd.backlight();
    lcd.clear();
}

void DisplayI2C::printPIN(std::string buffer)
{
    String temp = buffer.c_str();
    lcd.setCursor(0, 1);
    lcd.print(temp);
}

void DisplayI2C::showStatus(std::string message, int delayMs)
{
    String temp = message.c_str();
    lcd.clear();
    lcd.setCursor(0, 0);
    lcd.print(temp);

    if (delayMs > 0)
    {
        delay(delayMs);
        lcd.clear();
    }
}

void DisplayI2C::clearLine(uint8_t row)
{
    lcd.setCursor(0, row);
    lcd.print("                "); // Overwrite line with blank spaces
}
