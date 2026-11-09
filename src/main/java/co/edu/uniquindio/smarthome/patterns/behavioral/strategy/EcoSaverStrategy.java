package co.edu.uniquindio.smarthome.patterns.behavioral.strategy;

import co.edu.uniquindio.smarthome.model.SmartDevice;
import co.edu.uniquindio.smarthome.model.devices.SmartLight;
import co.edu.uniquindio.smarthome.model.devices.SmartThermostat;
import co.edu.uniquindio.smarthome.patterns.structural.adapter.LegacyDeviceAdapter;
import co.edu.uniquindio.smarthome.patterns.structural.composite.RoomComposite;
import co.edu.uniquindio.smarthome.patterns.structural.composite.SmartComponent;

import java.util.List;

/**
 * Estrategia de Ahorro Máximo (Eco-Saver):
 * - Atenúa luces al 50%.
 * - Ajusta termostatos a 24.5°C en modo ECO.
 * - Reduce intensidad del aire acondicionado clásico adaptado.
 */
public class EcoSaverStrategy implements IEnergyStrategy {

    @Override
    public String getName() {
        return "Modo Ecológico (Eco-Saver)";
    }

    @Override
    public String getDescription() {
        return "Minimiza la huella de carbono. Atenúa iluminación y ajusta termostatos a modo eficiente.";
    }

    @Override
    public String getIcon() {
        return "🌱";
    }

    @Override
    public double getEstimatedSavingsPercentage() {
        return 42.0; // 42% de ahorro
    }

    @Override
    public void apply(List<SmartComponent> components) {
        applyRecursive(components);
    }

    private void applyRecursive(List<SmartComponent> components) {
        for (SmartComponent comp : components) {
            if (comp instanceof SmartLight light) {
                if (light.isOn()) {
                    light.setBrightnessPercentage(50);
                }
            } else if (comp instanceof SmartThermostat thermostat) {
                thermostat.setHvacMode("ECO");
                thermostat.setTargetTemperature(24.5);
            } else if (comp instanceof LegacyDeviceAdapter legacyAc) {
                legacyAc.setCoolingIntensity(1);
            } else if (comp instanceof RoomComposite room) {
                applyRecursive(room.getChildren());
            }
        }
    }
}
