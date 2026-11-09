package co.edu.uniquindio.smarthome.patterns.behavioral.strategy;

import co.edu.uniquindio.smarthome.model.devices.SmartLight;
import co.edu.uniquindio.smarthome.model.devices.SmartThermostat;
import co.edu.uniquindio.smarthome.patterns.structural.adapter.LegacyDeviceAdapter;
import co.edu.uniquindio.smarthome.patterns.structural.composite.RoomComposite;
import co.edu.uniquindio.smarthome.patterns.structural.composite.SmartComponent;

import java.util.List;

/**
 * Estrategia Nocturna / Fuera de Casa (Night & Security):
 * Apaga luces interiores, baja la climatización para dormir o estar fuera.
 */
public class NightSecurityStrategy implements IEnergyStrategy {

    @Override
    public String getName() {
        return "Modo Noche & Seguridad";
    }

    @Override
    public String getDescription() {
        return "Apaga iluminación general y ajusta termostatos a 20°C para descanso nocturno.";
    }

    @Override
    public String getIcon() {
        return "🌙";
    }

    @Override
    public double getEstimatedSavingsPercentage() {
        return 55.0; // 55% de ahorro
    }

    @Override
    public void apply(List<SmartComponent> components) {
        applyRecursive(components);
    }

    private void applyRecursive(List<SmartComponent> components) {
        for (SmartComponent comp : components) {
            if (comp instanceof SmartLight light) {
                light.turnOff();
            } else if (comp instanceof SmartThermostat thermostat) {
                thermostat.setHvacMode("ECO");
                thermostat.setTargetTemperature(20.0);
            } else if (comp instanceof LegacyDeviceAdapter legacyAc) {
                legacyAc.turnOff();
            } else if (comp instanceof RoomComposite room) {
                applyRecursive(room.getChildren());
            }
        }
    }
}
