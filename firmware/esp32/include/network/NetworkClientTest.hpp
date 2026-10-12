#ifndef NETWORK_CLIENT_TEST_HPP
#define NETWORK_CLIENT_TEST_HPP

#include "network/INetworkClient.hpp"

namespace network{

    class NetworkClientTest: public INetworkClient{
        public:
            // resultado que devuelve siempre
            AccessResult resultado;
            NetworkClientTest() = default;
            // sobreescribo el metodo original com el valor fijo
            AccessResult postAccessValidation(const std::string& pin) override {
                return resultado;
            }
    };
} // namespace network

#endif // NETWORK_CLIENT_TEST_HPP
