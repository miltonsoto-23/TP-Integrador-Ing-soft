#include <gtest/gtest.h>
#include <ArduinoFake.h>
using namespace fakeit;
//#include "sensors/mock_sensor_model.hpp"
#include "network/NetworkClientTest.hpp"
#include "display/FakeDisplay.hpp"
#include "keypad/FakeKeypad.hpp"
#include "actuators/FakeActuators.hpp"
#include "access_control/AccessControl.hpp"

namespace networkClient
{
    namespace
    {
    // TEST de AccessControl
    TEST(AccessControlTest, PinCorrectoRetornaAccesoConcedido){
        ArduinoFakeReset(); // Reset los fakes de arduino entre tests
        // si tu código llama millis() o Serial.println() internamente,
        // les decís qué devolver para que no truene
        When(Method(ArduinoFake(), millis)).AlwaysReturn(0);
        // creo stub y fakes para simular hardware display y teclado
        network::NetworkClientTest stubNetwork;
        FakeKeypad fakeKeypad;
        FakeDisplay fakeDisplay;
        FakeActuators fakeActuators;
        // seteo el struct data
        stubNetwork.resultado = {"Acceso concedido", true, "2026-05-31T20:00:00Z", "", 0, "null"};
        // creo el AccessControl inyectando el stub y fakes
        AccessControl  acess(fakeDisplay, fakeKeypad, stubNetwork, fakeActuators);
        // en este caso simulo la tecla por tecla tiene que ser el pin correcto
        fakeKeypad.nextKey = '1' ; acess.update();
        fakeKeypad.nextKey = '2' ; acess.update(); // ejecuto el metodo update para registrar
        fakeKeypad.nextKey = '3' ; acess.update();
        fakeKeypad.nextKey = '4' ; acess.update();
        fakeKeypad.nextKey = '#' ; acess.update(); // confirma
        // Verifico el resultado
        EXPECT_EQ(fakeDisplay.lastMessage, "Access Granted");
    }
    TEST(AccessControlTest, TresIntentosFallidosLocked){
        ArduinoFakeReset(); // Reset los fakes de arduino entre tests
        // si tu código llama millis() o Serial.println() internamente,
        // les decís qué devolver para que no truene
        When(Method(ArduinoFake(), millis)).AlwaysReturn(0);
        // creo stub y fakes
        network::NetworkClientTest stubNetwork;
        FakeKeypad fakeKeypad;
        FakeDisplay fakeDisplay;
        FakeActuators fakeActuators;
        // seteo el struc locked
        stubNetwork.resultado = {"", false, "2026-05-31T20:00:00Z", "Sistema bloqueado", 0, "2026-05-31T20:00:40Z"};
        // Creo AccessControl inyectando stub y fakes
        AccessControl acess(fakeDisplay, fakeKeypad, stubNetwork, fakeActuators);
        // simulo el tecleo
        fakeKeypad.nextKey = '1' ; acess.update();
        fakeKeypad.nextKey = '2' ; acess.update(); // ejecuto el metodo update para registrar
        fakeKeypad.nextKey = '3' ; acess.update();
        fakeKeypad.nextKey = '4' ; acess.update();
        fakeKeypad.nextKey = '#' ; acess.update(); // confirma
        // ejecuto metodo que llama a postAccessvalidation

        // verifico resultado
        EXPECT_EQ(fakeDisplay.lastMessage, "SYSTEM BLOCKED");
    }
    TEST(AccessControlTest, IntentoFallido){
        network::NetworkClientTest stubNetwork;
        FakeKeypad fakeKeypad;
        FakeDisplay fakeDisplay;
        FakeActuators fakeActuators;
        // seteo el struc locked
        stubNetwork.resultado = {"", false, "", "PIN incorrecto, Acceso denegado", 2, ""};
        // Creo AccessControl inyectando stub y fakes
        AccessControl acess(fakeDisplay, fakeKeypad, stubNetwork, fakeActuators);
        //simulo
        fakeKeypad.nextKey = '1' ; acess.update();
        fakeKeypad.nextKey = '9' ; acess.update(); // ejecuto el metodo update para registrar
        fakeKeypad.nextKey = '5' ; acess.update();
        fakeKeypad.nextKey = '4' ; acess.update();
        fakeKeypad.nextKey = '#' ; acess.update(); // confirma
        // ejecuto metodo que llama a postAccessvalidation

        // verifico resultado
        EXPECT_EQ(fakeDisplay.lastMessage,"Error (" + std::to_string(stubNetwork.resultado.intentosRestantes) + " left)");
    }
    } // namespace

} // namespace networkClient

int main(int argc, char** argv) {
    ::testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}
