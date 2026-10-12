#include "network/networkClient.hpp"
#include "appConfig.hpp"
#include <ArduinoJson.h>
#include <HTTPClient.h>
#include <WiFi.h>
#include <sensors/EnvironmentSensor.hpp>

auto constexpr WIFI_CONNECTION_TIMEOUT_MS = 15000UL;
auto constexpr HTTP_STATUS_OK = 200;
auto constexpr HTTP_STATUS_MULTIPLE_CHOICES = 300;

namespace network
{

    NetworkClient::NetworkClient(const app::AppConfig& config)
        : config_(config)
    {
    }

    void NetworkClient::begin()
    {
        connectToWifi();
    }

    void NetworkClient::ensureWifiConnection()
    {
        if (WiFi.status() != WL_CONNECTED)
        {
            connectToWifi();
        }
    }

    bool NetworkClient::isConnected() const
    {
        return WiFi.status() == WL_CONNECTED;
    }

    bool NetworkClient::postSensorReading(const sensors::SensorData& data)
    {
        if (!isConnected())
        {
            return false;
        }

        HTTPClient http;
        const String endpoint = String(config_.backendBaseUrl) + "/api/envio";

        if (!http.begin(endpoint))
        {
            logMessage("Could not open telemetry endpoint.");
            return false;
        }

        http.addHeader("Content-Type", "application/json");

        JsonDocument payload;
        payload["sensor1Id"] = data.sensor1Id;     // { "sensor1Id":"lm35"
        payload["sensor2Id"] = data.sensor2Id;     //   "sensor2Id":"mq7"
        payload["temperature"] = data.temperature; //   "temperature":"24.5"
        payload["smoke"] = data.percentage;        //    "smoke":"55.22"}

        String body;
        serializeJson(payload, body);

        const int statusCode = http.POST(body);
        const String responseBody = http.getString();
        http.end();

        if (statusCode >= HTTP_STATUS_OK && statusCode < HTTP_STATUS_MULTIPLE_CHOICES)
        {
            logMessage("Telemetry sent. temp=" + String(data.temperature, 1) + "C smoke=" + String(data.percentage, 1) +
                       "% body=" + responseBody);
            return true;
        }

        logMessage("Telemetry failed. HTTP " + String(statusCode) + " body=" + responseBody);
        return false;
    }

    AccessResult NetworkClient::postAccessValidation(const std::string& pin)
    {
        AccessResult result {"", false, "", "", 0, ""};
        if (!isConnected())
        {
            return result;
        }
        HTTPClient http;
        const String endpoint = String(config_.backendBaseUrl) + "/api/acceso/validar";
        if (!http.begin(endpoint))
        {
            logMessage("Could not open Access Validation endpoint");
            return result;
        }

        http.addHeader("Content-Type", "application/json");
        JsonDocument payload;
        payload["pinIngresado"] = pin; // {"pinIngresado":"pin"}
        String body;
        serializeJson(payload, body);
        const int statusCode = http.POST(body);
        const String responseBody = http.getString();
        http.end();
        JsonDocument doc;
        DeserializationError e = deserializeJson(doc, responseBody);

        if (e)
        {
            result.error = "JSON invalido por parte del backend";
            return result;
        }

        if (statusCode >= HTTP_STATUS_OK && statusCode < HTTP_STATUS_MULTIPLE_CHOICES)
        {
            logMessage("valid pin");
            result.mensaje = doc["mensaje"] | "";
            result.acceso = doc["acceso"] | false;
            result.timestamp = doc["timestamp"] | "";
            return result;
        }
        else if (statusCode == 400)
        {
            result.error = doc["error"] | "";
            logMessage("required pin. HTTP " + String(statusCode) + " body=" + String(result.error.c_str()));
            return result;
        }
        else if (statusCode == 401)
        {
            result.acceso = doc["acceso"] | false;
            result.error = doc["error"] | "";
            result.intentosRestantes = doc["intentosRestantes"] | 0;
            logMessage("PIN not authorized. HTTP " + String(statusCode) + " body=" + String(result.error.c_str()));
            return result;
        }
        else if (statusCode == 423)
        {
            result.acceso = doc["acceso"] | false;
            result.error = doc["error"] | "";
            result.intentosRestantes = doc["intentosRestantes"] | 0;
            result.bloqueadoHasta = doc["bloqueadoHasta"] | "";
            logMessage("Locked. HTTP " + String(statusCode) + " body=" + String(result.error.c_str()));
            return result;
        }
        result.error = doc["error"] | "";
        logMessage("Internal Error. HTTP " + String(statusCode) + " body=" + String(result.error.c_str()));
        return result;
    }

    void NetworkClient::connectToWifi()
    {
        if (WiFi.status() == WL_CONNECTED)
        {
            return;
        }

        logMessage("Connecting to Wi-Fi...");
        WiFi.mode(WIFI_STA);
        WiFi.begin(config_.wifiSsid, config_.wifiPassword);

        const unsigned long startedAt = millis();
        while (WiFi.status() != WL_CONNECTED && millis() - startedAt < WIFI_CONNECTION_TIMEOUT_MS)
        {
            delay(500);
            Serial.print('.');
        }
        Serial.println();

        if (WiFi.status() == WL_CONNECTED)
        {
            logMessage("Wi-Fi connected. IP: " + WiFi.localIP().toString());
            return;
        }

        logMessage("Wi-Fi connection failed. Will retry on next loop.");
    }

    void NetworkClient::logMessage(const String& message)
    {
        Serial.println(String("[ESP32] ") + message);
    }

} // namespace network
