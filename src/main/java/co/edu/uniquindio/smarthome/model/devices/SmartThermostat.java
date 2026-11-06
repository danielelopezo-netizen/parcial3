package co.edu.uniquindio.smarthome.model.devices;

import co.edu.uniquindio.smarthome.model.DeviceType;
import co.edu.uniquindio.smarthome.model.SmartDevice;

public class SmartThermostat extends SmartDevice {
    private double targetTemperature;
    private double currentTemperature;
    private String hvacMode; // "ECO", "COOL", "HEAT", "AUTO"

    public SmartThermostat(String id, String name, String roomLocation, double basePowerWatts) {
        super(id, name, DeviceType.THERMOSTAT, roomLocation, basePowerWatts);
        this.targetTemperature = 22.0;
        this.currentTemperature = 24.5;
        this.hvacMode = "AUTO";
        this.on = true; // Termostatos suelen estar siempre monitorizando
    }

    public double getTargetTemperature() {
        return targetTemperature;
    }

    public void setTargetTemperature(double targetTemperature) {
        this.targetTemperature = Math.max(16.0, Math.min(30.0, targetTemperature));
    }

    public double getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(double currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public String getHvacMode() {
        return hvacMode;
    }

    public void setHvacMode(String hvacMode) {
        this.hvacMode = hvacMode;
    }

    @Override
    public double getPowerConsumptionWatts() {
        if (!on) return 1.0;
        double diff = Math.abs(currentTemperature - targetTemperature);
        return basePowerWatts * (1.0 + (diff * 0.15));
    }

    @Override
    public String getSpecificStatusDescription() {
        if (!on) return "Climatizador Apagado";
        return String.format("Objetivo: %.1f°C (Actual: %.1f°C) [%s]", targetTemperature, currentTemperature, hvacMode);
    }
}
