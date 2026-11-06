package co.edu.uniquindio.smarthome.model.devices;

import co.edu.uniquindio.smarthome.model.DeviceType;
import co.edu.uniquindio.smarthome.model.SmartDevice;

public class SmartLock extends SmartDevice {
    private boolean locked;
    private int batteryPercentage;
    private String securityPin;

    public SmartLock(String id, String name, String roomLocation, double basePowerWatts) {
        super(id, name, DeviceType.LOCK, roomLocation, basePowerWatts);
        this.locked = true;
        this.batteryPercentage = 95;
        this.securityPin = "1234";
        this.on = true; // Activa siempre para recibir comandos
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void lock() {
        this.locked = true;
    }

    public void unlock() {
        this.locked = false;
    }

    public int getBatteryPercentage() {
        return batteryPercentage;
    }

    public boolean validatePin(String pin) {
        return this.securityPin != null && this.securityPin.equals(pin);
    }

    public void setSecurityPin(String securityPin) {
        this.securityPin = securityPin;
    }

    @Override
    public double getPowerConsumptionWatts() {
        return 0.8; // Muy bajo consumo en espera
    }

    @Override
    public String getSpecificStatusDescription() {
        return (locked ? "🔒 BLOQUEADA (Segura)" : "🔓 DESBLOQUEADA") + " | Batería: " + batteryPercentage + "%";
    }
}
