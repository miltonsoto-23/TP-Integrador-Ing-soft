#ifndef MATRIX_KEYPAD_HPP
#define MATRIX_KEYPAD_HPP

#include <Arduino.h>
#include <Keypad.h>
#include "IKeypad.hpp"

class MatrixKeypad : public IKeypad
{
private:
    static const byte ROWS = 4;
    static const byte COLS = 4;

    char keys[ROWS][COLS];
    byte rowPins[ROWS];
    byte colPins[COLS];

    Keypad* keypad;

public:
    MatrixKeypad();
    ~MatrixKeypad() override;

    char getKey() override;
};

#endif // MATRIX_KEYPAD_HPP
