import React, { createContext, useContext, useState, useEffect } from 'react';
import * as mqtt from 'paho-mqtt';

const MQTTContext = createContext(null);

export const MQTTProvider = ({ children }) => {
  const [client, setClient] = useState(null);
  const [isConnected, setIsConnected] = useState(false);
  const [messages, setMessages] = useState([]);

  // Konfiguracja lokalnego brokera Mosquitto
  const brokerConfig = {
    host: 'localhost',  // Lokalny broker
    port: 9001,         // Domyślny port Mosquitto
    clientId: `tram-control-${Math.random().toString(36).substring(7)}`,
    path: '/mqtt',
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
        timestamp: new Date()
      };
      setMessages(prev => [...prev, newMessage]);
    };

    const connectOptions = {
      onSuccess: () => {
        console.log('Połączono z lokalnym brokerem MQTT');
        setIsConnected(true);
        
        // Subskrypcja tematów
        mqttClient.subscribe('tram/switches/#');
        mqttClient.subscribe('tram/heating/#');
      },
      onFailure: (error) => {
        console.error('Błąd połączenia:', error);
        setIsConnected(false);
      },
    };

    mqttClient.connect(connectOptions);
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
    <MQTTContext.Provider value={{ 
      client, 
      isConnected, 
      messages, 
      publishMessage 
    }}>
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