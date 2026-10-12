#ifndef FAKE_KEYPAD_HPP
#define FAKE_KEYPAD_HPP

#include "IKeypad.hpp"

class FakeKeypad : public IKeypad{
    public:
        char nextKey = '\0';

        FakeKeypad(){}
        ~FakeKeypad() override{}
        char getKey() override {
            return nextKey;
        }
};

#endif // FAKE_KEYPAD_HPP
