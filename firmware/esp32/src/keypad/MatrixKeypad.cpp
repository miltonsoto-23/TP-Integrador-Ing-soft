#include "keypad/MatrixKeypad.hpp"

MatrixKeypad::MatrixKeypad()
{
    // Standard 4x4 matrix mapping
    char defaultKeys[ROWS][COLS] = {
        {'1', '2', '3', 'A'}, {'4', '5', '6', 'B'}, {'7', '8', '9', 'C'}, {'*', '0', '#', 'D'}};

    memcpy(keys, defaultKeys, sizeof(keys));

    // ESP32 GPIO allocation for Rows and Columns
    byte defaultRowPins[ROWS] = {19, 18, 5, 17};
    byte defaultColPins[COLS] = {16, 4, 2, 15};

    memcpy(rowPins, defaultRowPins, sizeof(rowPins));
    memcpy(colPins, defaultColPins, sizeof(colPins));

    // Instantiate Keypad engine
    keypad = new Keypad(makeKeymap(keys), rowPins, colPins, ROWS, COLS);
}

MatrixKeypad::~MatrixKeypad()
{
    delete keypad;
}

char MatrixKeypad::getKey()
{
    return keypad->getKey();
}
