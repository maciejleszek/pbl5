import React, { useState, useEffect, useRef } from 'react';
import { ThermometerSun } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from "./ui/card";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "./ui/tabs";
import { Alert, AlertDescription, AlertTitle } from "./ui/alert";

const TramControlSystem = () => {
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
    addLog(`Zmiana położenia zwrotnicy ${switchId}`);
  };

  const toggleHeating = (forced) => {
    if (!hasPermission('heating_control')) return;
    setHeatingParams(prev => ({ ...prev, isHeatingForced: forced }));
    addLog(`${forced ? 'Wymuszenie' : 'Wyłączenie'} ogrzewania`);
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

  return (
    <div className="p-4 max-w-6xl mx-auto">
      <Tabs defaultValue="map" className="space-y-4">
        <TabsList className="tabs-list">
          <TabsTrigger value="map" className="tab-trigger">Mapa</TabsTrigger>
          <TabsTrigger value="heating" className="tab-trigger">Ogrzewanie</TabsTrigger>
          <TabsTrigger value="settings" className="tab-trigger">Ustawienia</TabsTrigger>
          <TabsTrigger value="logs" className="tab-trigger">Logi</TabsTrigger>
        </TabsList>

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

        <TabsContent value="heating">
          <Card className="card">
            <CardHeader>
              <CardTitle>Sterowanie ogrzewaniem</CardTitle>
            </CardHeader>
            <CardContent>
              {hasPermission('heating_control') ? (
                <div className="space-y-4">
                  <div className="flex items-center gap-4">
                    <ThermometerSun size={24} />
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
                <p>Brak uprawnień</p>
              )}
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="settings">
          <Card className="card">
            <CardHeader>
              <CardTitle>Parametry ogrzewania</CardTitle>
            </CardHeader>
            <CardContent>
              {hasPermission('params_control') ? (
                <div className="space-y-4">
                  <div>
                    <label>Próg temperaturowy (°C)</label>
                    <input
                      type="number"
                      value={heatingParams.tempThreshold}
                      onChange={(e) => {
                        setHeatingParams(prev => ({ ...prev, tempThreshold: parseFloat(e.target.value) }));
                        addLog(`Zmiana progu temperaturowego: ${e.target.value}°C`);
                      }}
                      className="input"
                    />
                  </div>
                  <div>
                    <label>Histereza (°C)</label>
                    <input
                      type="number"
                      value={heatingParams.hysteresis}
                      onChange={(e) => {
                        setHeatingParams(prev => ({ ...prev, hysteresis: parseFloat(e.target.value) }));
                        addLog(`Zmiana histerezy: ${e.target.value}°C`);
                      }}
                      className="input"
                    />
                  </div>
                  <div>
                    <label>Min. czas ogrzewania (min)</label>
                    <input
                      type="number"
                      value={heatingParams.minHeatingTime}
                      onChange={(e) => {
                        setHeatingParams(prev => ({ ...prev, minHeatingTime: parseInt(e.target.value) }));
                        addLog(`Zmiana min. czasu ogrzewania: ${e.target.value}min`);
                      }}
                      className="input"
                    />
                  </div>
                  <div>
                    <label>Max. czas ogrzewania (min)</label>
                    <input
                      type="number"
                      value={heatingParams.maxHeatingTime}
                      onChange={(e) => {
                        setHeatingParams(prev => ({ ...prev, maxHeatingTime: parseInt(e.target.value) }));
                        addLog(`Zmiana max. czasu ogrzewania: ${e.target.value}min`);
                      }}
                      className="input"
                    />
                  </div>
                </div>
              ) : (
                <p>Brak uprawnień</p>
              )}
            </CardContent>
          </Card>
        </TabsContent>

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
      </Tabs>
    </div>
  );
};

export default TramControlSystem;