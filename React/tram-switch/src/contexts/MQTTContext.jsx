import React, { createContext, useContext, useState, useEffect } from 'react';
import * as mqtt from 'paho-mqtt';

// Create MQTT Context
const MQTTContext = createContext(null);

export const MQTTProvider = ({ children }) => {
  const [client, setClient] = useState(null);
  const [isConnected, setIsConnected] = useState(false);
  const [messages, setMessages] = useState([]);

  // MQTT Broker Configuration (Replace with your actual broker details)
  const brokerConfig = {
    host: 'your-mqtt-broker-host',
    port: 8883,  // Secure WebSocket port
    clientId: `tram-control-${Math.random().toString(36).substring(7)}`,
    username: 'your-username',
    password: 'your-password'
  };

  useEffect(() => {
    const mqttClient = new mqtt.Client(
      brokerConfig.host, 
      brokerConfig.port, 
      brokerConfig.clientId
    );

    mqttClient.onConnectionLost = (responseObject) => {
      if (responseObject.errorCode !== 0) {
        console.log('Connection lost:', responseObject.errorMessage);
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
        console.log('Connected to MQTT Broker');
        setIsConnected(true);
        
        // Subscribe to relevant topics
        mqttClient.subscribe('tram/switches/#');
        mqttClient.subscribe('tram/heating/#');
      },
      onFailure: (error) => {
        console.error('Connection failed:', error);
        setIsConnected(false);
      },
      userName: brokerConfig.username,
      password: brokerConfig.password,
      useSSL: true
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

// Custom hook for easy MQTT context access
export const useMQTT = () => {
  const context = useContext(MQTTContext);
  if (!context) {
    throw new Error('useMQTT must be used within an MQTTProvider');
  }
  return context;
};