#ifndef I_NETWORK_CLIENT_HPP
#define I_NETWORK_CLIENT_HPP
#include <string>
/*#ifndef ARDUINO
    #include <string>
    typedef std::string String;
#else
    #include <Arduino.h>
#endif */

namespace network {

    struct AccessResult{
        std::string mensaje;
        bool acceso;
        std::string timestamp;
        std::string error;
        int intentosRestantes;
        std::string bloqueadoHasta;
    };

    class INetworkClient {
        public:
            virtual AccessResult postAccessValidation(const std::string& pin) = 0;
            virtual ~INetworkClient() = default;
    };
}

#endif //I_NETWORK_CLIENT_HPP
