import React, { useState, useEffect, useRef } from 'react';
import { ThermometerSun } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from "./ui/card";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "./ui/tabs";
import { Alert, AlertDescription, AlertTitle } from "./ui/alert";
import { useMQTT } from '../contexts/MQTTLocalContext';

const TramControlSystem = () => {
  const { publishMessage, messages, isConnected } = useMQTT();
  const [selectedSwitch, setSelectedSwitch] = useState(null);
  const [heatingParams, setHeatingParams] = useState({
    tempThreshold: 2,
    hysteresis: 1,
    minHeatingTime: 10,
    maxHeatingTime: 30,
    isHeatingForced: false
  });
  const [logs, setLogs] = useState([]);
  const [userRole] = useState('admin');
  const mapRef = useRef(null);
  const mapInstanceRef = useRef(null);

  const switches = [
    { id: 1, name: 'Zwrotnica 1', position: 'prawa', temp: -1, heatingOn: true, lat: 52.2297, lon: 21.0122 },
    { id: 2, name: 'Zwrotnica 2', position: 'lewa', temp: 3, heatingOn: false, lat: 52.2299, lon: 21.0125 },
  ];

  const addLog = (action) => {
    const timestamp = new Date().toISOString();
    setLogs(prev => [...prev, { timestamp, action }]);
  };

  const toggleSwitchPosition = (switchId) => {
    if (!hasPermission('switch_control')) return;
    const message = {
      switchId,
      action: 'toggle',
      timestamp: new Date().toISOString()
    };
    publishMessage(`tram/switches/${switchId}/position`, message);
    addLog(`Zmiana położenia zwrotnicy ${switchId}`);
  };

  const toggleHeating = (forced) => {
    if (!hasPermission('heating_control')) return;
    const message = {
      action: forced ? 'force_on' : 'auto',
      timestamp: new Date().toISOString()
    };
    publishMessage('tram/heating/control', message);
    setHeatingParams(prev => ({ ...prev, isHeatingForced: forced }));
    addLog(`${forced ? 'Wymuszenie' : 'Wyłączenie'} ogrzewania`);
  };

  // Update the heating parameters method
  const updateHeatingParams = (key, value) => {
    if (!hasPermission('params_control')) return;
    
    // Convert input to appropriate number type
    const numericValue = key.includes('Time') 
      ? parseInt(value, 10) 
      : parseFloat(value);
    
    const message = {
      [key]: numericValue,
      timestamp: new Date().toISOString()
    };
    
    publishMessage('tram/heating/params', message);
    
    setHeatingParams(prev => ({ 
      ...prev, 
      [key]: numericValue 
    }));
    
    addLog(`Zmiana parametru ${key}: ${numericValue}`);
  };

  const hasPermission = (permission) => {
    const permissions = {
      admin: ['switch_control', 'heating_control', 'params_control'],
      operator: ['switch_control'],
      technician: ['heating_control', 'params_control']
    };
    return permissions[userRole]?.includes(permission);
  };

  const MapComponent = () => {
    useEffect(() => {
      const initMap = async () => {
        if (!mapInstanceRef.current && mapRef.current) {
          const L = (await import('leaflet')).default;
          
          const link = document.createElement('link');
          link.rel = 'stylesheet';
          link.href = 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/leaflet.css';
          document.head.appendChild(link);
          
          mapInstanceRef.current = L.map(mapRef.current).setView([52.2297, 21.0122], 15);
          
          L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '© OpenStreetMap contributors'
          }).addTo(mapInstanceRef.current);

          switches.forEach(sw => {
            const marker = L.marker([sw.lat, sw.lon])
              .addTo(mapInstanceRef.current)
              .bindPopup(sw.name)
              .on('click', () => setSelectedSwitch(sw));

            if (sw.heatingOn) {
              const redIcon = L.divIcon({
                html: '<div class="switch-marker switch-marker-active"></div>',
                className: ''
              });
              marker.setIcon(redIcon);
            }
          });
        }
      };

      initMap();

      return () => {
        if (mapInstanceRef.current) {
          mapInstanceRef.current.remove();
          mapInstanceRef.current = null;
        }
      };
    }, []);

    return <div ref={mapRef} className="map-container" />;
  };

  const renderMQTTMessages = () => (
    <TabsContent value="mqtt">
      <Card>
        <CardHeader>
          <CardTitle>MQTT Messages</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-2">
            {messages.map((msg, index) => (
              <div key={index} className="log-entry">
                <span className="log-timestamp">{msg.timestamp.toLocaleString()}</span>
                <span>{msg.topic}: {JSON.stringify(msg.payload)}</span>
              </div>
            ))}
          </div>
          <div className="mt-4">
            Connection Status: {isConnected ? '✅ Connected' : '❌ Disconnected'}
          </div>
        </CardContent>
      </Card>
    </TabsContent>
  );

  return (
    <div className="p-4 max-w-6xl mx-auto">
      <Tabs defaultValue="home" className="space-y-4">
        <TabsList className="tabs-list">
          <TabsTrigger value="home" className="tab-trigger">Strona główna</TabsTrigger>
          <TabsTrigger value="map" className="tab-trigger">Mapa</TabsTrigger>
          <TabsTrigger value="heating" className="tab-trigger">Ogrzewanie</TabsTrigger>
          <TabsTrigger value="settings" className="tab-trigger">Ustawienia</TabsTrigger>
          <TabsTrigger value="logs" className="tab-trigger">Logi</TabsTrigger>
          <TabsTrigger value="mqtt" className="tab-trigger">MQTT</TabsTrigger>
        </TabsList>

        {/* Home Page Tab */}
        <TabsContent value="home">
          <Card className="card">
            <CardHeader>
              <CardTitle>System Sterowania Tramwajami</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="bg-gradient-to-r from-blue-100 to-blue-200 p-6 rounded-lg shadow-md">
                <h2 className="text-2xl font-bold mb-4 text-blue-900">Witaj w Systemie Zdalnego Sterowania</h2>
                <p className="text-blue-800 mb-4">
                  Kompleksowe narzędzie do monitorowania i zarządzania infrastrukturą tramwajową.
                </p>
                <div className="grid md:grid-cols-3 gap-4">
                  <div className="bg-white p-4 rounded-lg shadow-sm">
                    <h3 className="font-semibold text-blue-700 mb-2">Mapa Rozjazdów</h3>
                    <p className="text-sm text-blue-600">Podgląd aktualnego położenia zwrotnic</p>
                  </div>
                  <div className="bg-white p-4 rounded-lg shadow-sm">
                    <h3 className="font-semibold text-blue-700 mb-2">Sterowanie Ogrzewaniem</h3>
                    <p className="text-sm text-blue-600">Zaawansowane opcje kontroli temperatury</p>
                  </div>
                  <div className="bg-white p-4 rounded-lg shadow-sm">
                    <h3 className="font-semibold text-blue-700 mb-2">Logi Operacji</h3>
                    <p className="text-sm text-blue-600">Pełna historia zmian systemowych</p>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Heating Settings Tab */}
        <TabsContent value="heating">
          <Card className="card">
            <CardHeader>
              <CardTitle>Sterowanie Ogrzewaniem</CardTitle>
            </CardHeader>
            <CardContent>
              {hasPermission('heating_control') ? (
                <div className="space-y-4">
                  <div className="flex items-center gap-4">
                    <ThermometerSun size={24} className="text-blue-500" />
                    <div>
                      <p>Status: {heatingParams.isHeatingForced ? 'Wymuszone' : 'Automatyczne'}</p>
                      <div className="mt-2 space-x-2">
                        <button
                          onClick={() => toggleHeating(true)}
                          className="btn btn-danger"
                        >
                          Wymuś włączenie
                        </button>
                        <button
                          onClick={() => toggleHeating(false)}
                          className="btn btn-primary"
                        >
                          Tryb auto
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              ) : (
                <p className="text-red-500">Brak uprawnień do sterowania ogrzewaniem</p>
              )}
            </CardContent>
          </Card>
        </TabsContent>

        {/* Settings Tab */}
        <TabsContent value="settings">
          <Card className="card">
            <CardHeader>
              <CardTitle>Parametry Ogrzewania</CardTitle>
            </CardHeader>
            <CardContent>
              {hasPermission('params_control') ? (
                <div className="space-y-4">
                  {Object.keys(heatingParams)
                    .filter(key => key !== 'isHeatingForced')
                    .map(key => (
                      <div key={key} className="grid grid-cols-2 gap-4 items-center">
                        <label className="text-blue-800 font-medium">
                          {key === 'tempThreshold' && 'Próg temperaturowy (°C)'}
                          {key === 'hysteresis' && 'Histereza (°C)'}
                          {key === 'minHeatingTime' && 'Min. czas ogrzewania (min)'}
                          {key === 'maxHeatingTime' && 'Max. czas ogrzewania (min)'}
                        </label>
                        <input
                          type="number"
                          value={heatingParams[key]}
                          onChange={(e) => updateHeatingParams(key, e.target.value)}
                          className="input border-blue-300 focus:border-blue-500"
                          step={key.includes('Time') ? 1 : 0.1}
                        />
                      </div>
                    ))}
                </div>
              ) : (
                <p className="text-red-500">Brak uprawnień do zmiany parametrów</p>
              )}
            </CardContent>
          </Card>
        </TabsContent>

        {/* Map Tab */}
        <TabsContent value="map">
          <Card className="card">
            <CardHeader>
              <CardTitle>Mapa rozjazdów</CardTitle>
            </CardHeader>
            <CardContent>
              <MapComponent />
              {selectedSwitch && (
                <Alert className="alert">
                  <AlertTitle className="alert-title">{selectedSwitch.name}</AlertTitle>
                  <AlertDescription>
                    <div className="flex justify-between items-center">
                      <div>
                        <p>Położenie: {selectedSwitch.position}</p>
                        <p>Temperatura: {selectedSwitch.temp}°C</p>
                        <p>Ogrzewanie: {selectedSwitch.heatingOn ? 'Włączone' : 'Wyłączone'}</p>
                      </div>
                      {hasPermission('switch_control') && (
                        <button
                          onClick={() => toggleSwitchPosition(selectedSwitch.id)}
                          className="btn btn-primary"
                        >
                          Przełóż zwrotnicę
                        </button>
                      )}
                    </div>
                  </AlertDescription>
                </Alert>
              )}
            </CardContent>
          </Card>
        </TabsContent>
                
        {/* Logs Tab */}
        <TabsContent value="logs">
          <Card className="card">
            <CardHeader>
              <CardTitle>Historia operacji</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-2">
                {logs.map((log, index) => (
                  <div key={index} className="log-entry">
                    <span className="log-timestamp">{new Date(log.timestamp).toLocaleString()}</span>
                    <span>{log.action}</span>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>
        {renderMQTTMessages()}
      </Tabs>
    </div>
  );
};

export default TramControlSystem;