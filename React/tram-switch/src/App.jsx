import { MQTTProvider } from './contexts/MQTTLocalContext'
import TramControlSystem from './components/TramControlSystem'

function App() {
  return (
    <MQTTProvider>
      <TramControlSystem />
    </MQTTProvider>
  )
}

export default App