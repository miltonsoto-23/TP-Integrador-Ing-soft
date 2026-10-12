#ifndef I_KEYPAD_HPP
#define I_KEYPAD_HPP

class IKeypad{
    public:
        // destructor
        virtual ~IKeypad(){}
        // metodo a implementar
        virtual char getKey() =0;
};
#endif // I_KEYPAD_HPP
