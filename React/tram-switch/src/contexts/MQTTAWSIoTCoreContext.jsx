import React, { createContext, useContext, useState, useEffect } from 'react';
import * as mqtt from 'paho-mqtt';

const MQTTContext = createContext(null);

export const MQTTProvider = ({ children }) => {
  const [client, setClient] = useState(null);
  const [isConnected, setIsConnected] = useState(false);
  const [messages, setMessages] = useState([]);

  // Konfiguracja AWS IoT Core
  const brokerConfig = {
    host: 'a13ssfxeh62jku-ats.iot.eu-central-1.amazonaws.com', // Zastąp swoim endpointem z AWS IoT Core
    port: 8883, // Port SSL
    clientId: `tram-control-${Math.random().toString(36).substring(7)}`,
    useSSL: true, // Wymagane dla AWS IoT Core
  };

  useEffect(() => {
    const mqttClient = new mqtt.Client(
      brokerConfig.host,
      brokerConfig.port,
      brokerConfig.clientId
    );

    mqttClient.onConnectionLost = (responseObject) => {
      if (responseObject.errorCode !== 0) {
        console.log('Utracono połączenie:', responseObject.errorMessage);
        setIsConnected(false);
      }
    };

    mqttClient.onMessageArrived = (message) => {
      const newMessage = {
        topic: message.destinationName,
        payload: message.payloadString,
        timestamp: new Date(),
      };
      setMessages((prev) => [...prev, newMessage]);
    };

    const connectOptions = {
      onSuccess: () => {
        console.log('Połączono z AWS IoT Core');
        setIsConnected(true);

        // Subskrypcja tematów
        mqttClient.subscribe('tram/switches/#');
        mqttClient.subscribe('tram/heating/#');
      },
      onFailure: (error) => {
        console.error('Błąd połączenia:', error);
        setIsConnected(false);
      },
      useSSL: true,
      mqttVersion: 4,
      userName: null, // Nie wymagany dla certyfikatów AWS IoT
      password: null,
    };

    mqttClient.connect({
      ...connectOptions,
      
      // Ścieżki do certyfikatów wymagane przez AWS IoT Core
      mqttVersionExplicit: true,
      onSuccess: connectOptions.onSuccess,
      onFailure: connectOptions.onFailure,
    });

    setClient(mqttClient);

    return () => {
      if (mqttClient && mqttClient.isConnected()) {
        mqttClient.disconnect();
      }
    };
  }, []);

  const publishMessage = (topic, payload, qos = 0) => {
    if (client && isConnected) {
      const message = new mqtt.Message(JSON.stringify(payload));
      message.destinationName = topic;
      message.qos = qos;
      client.send(message);
    }
  };

  return (
    <MQTTContext.Provider
      value={{
        client,
        isConnected,
        messages,
        publishMessage,
      }}
    >
      {children}
    </MQTTContext.Provider>
  );
};

export const useMQTT = () => {
  const context = useContext(MQTTContext);
  if (!context) {
    throw new Error('useMQTT must be used within an MQTTProvider');
  }
  return context;
};
