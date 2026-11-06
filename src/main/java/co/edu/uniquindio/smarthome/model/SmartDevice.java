package co.edu.uniquindio.smarthome.model;

import co.edu.uniquindio.smarthome.patterns.structural.composite.SmartComponent;

/**
 * Clase abstracta base para todos los dispositivos inteligentes del sistema.
 * Actúa como "Hoja" (Leaf) en el patrón Composite.
 */
public abstract class SmartDevice implements SmartComponent {
    protected final String id;
    protected String name;
    protected final DeviceType type;
    protected String roomLocation;
    protected boolean on;
    protected DeviceStatus status;
    protected double basePowerWatts;

    public SmartDevice(String id, String name, DeviceType type, String roomLocation, double basePowerWatts) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.roomLocation = roomLocation;
        this.basePowerWatts = basePowerWatts;
        this.on = false;
        this.status = DeviceStatus.ONLINE;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeviceType getType() {
        return type;
    }

    @Override
    public String getDisplayLocation() {
        return roomLocation;
    }

    public void setRoomLocation(String roomLocation) {
        this.roomLocation = roomLocation;
    }

    @Override
    public boolean isOn() {
        return on;
    }

    @Override
    public void turnOn() {
        this.on = true;
    }

    @Override
    public void turnOff() {
        this.on = false;
    }

    @Override
    public double getPowerConsumptionWatts() {
        return on ? basePowerWatts : 0.5; // Consumo en standby
    }

    @Override
    public int getComponentCount() {
        return 1;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public void setStatus(DeviceStatus status) {
        this.status = status;
    }

    public abstract String getSpecificStatusDescription();

    @Override
    public String toString() {
        return type.getIcon() + " " + name + " [" + roomLocation + "] - " + (on ? "ENCENDIDO" : "APAGADO");
    }
}
