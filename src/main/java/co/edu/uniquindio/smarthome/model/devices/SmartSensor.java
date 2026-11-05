package co.edu.uniquindio.smarthome.model.devices;

import co.edu.uniquindio.smarthome.model.DeviceType;
import co.edu.uniquindio.smarthome.model.SmartDevice;

public class SmartSensor extends SmartDevice {
    private boolean triggered;
    private long lastTriggerTimeMillis;

    public SmartSensor(String id, String name, DeviceType sensorType, String roomLocation) {
        super(id, name, sensorType, roomLocation, 1.2);
        this.triggered = false;
        this.lastTriggerTimeMillis = 0;
        this.on = true;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
        if (triggered) {
            this.lastTriggerTimeMillis = System.currentTimeMillis();
        }
    }

    public long getLastTriggerTimeMillis() {
        return lastTriggerTimeMillis;
    }

    @Override
    public double getPowerConsumptionWatts() {
        return 0.9; // Consumo continuo ultrabajo
    }

    @Override
    public String getSpecificStatusDescription() {
        if (triggered) {
            return type == DeviceType.SMOKE_SENSOR ? "🚨 ¡HUMO/GAS DETECTADO!" : "🚶 Movimiento Detectado";
        }
        return "Normal / Sin Detección";
    }
}
