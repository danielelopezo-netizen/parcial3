package co.edu.uniquindio.smarthome.patterns.behavioral.strategy;

import co.edu.uniquindio.smarthome.model.devices.SmartLight;
import co.edu.uniquindio.smarthome.model.devices.SmartThermostat;
import co.edu.uniquindio.smarthome.patterns.structural.adapter.LegacyDeviceAdapter;
import co.edu.uniquindio.smarthome.patterns.structural.composite.RoomComposite;
import co.edu.uniquindio.smarthome.patterns.structural.composite.SmartComponent;

import java.util.List;

/**
 * Estrategia de Confort Máximo:
 * Prioriza la comodidad y experiencia de los habitantes (21.5°C, iluminación brillante al 90%).
 */
public class ComfortPriorityStrategy implements IEnergyStrategy {

    @Override
    public String getName() {
        return "Modo Máximo Confort";
    }

    @Override
    public String getDescription() {
        return "Prioriza temperatura perfecta y luminosidad óptima sin restricciones energéticas.";
    }

    @Override
    public String getIcon() {
        return "🛋️";
    }

    @Override
    public double getEstimatedSavingsPercentage() {
        return 0.0; // Confort sin límite
    }

    @Override
    public void apply(List<SmartComponent> components) {
        applyRecursive(components);
    }

    private void applyRecursive(List<SmartComponent> components) {
        for (SmartComponent comp : components) {
            if (comp instanceof SmartLight light) {
                if (light.isOn()) {
                    light.setBrightnessPercentage(90);
                }
            } else if (comp instanceof SmartThermostat thermostat) {
                thermostat.setHvacMode("AUTO");
                thermostat.setTargetTemperature(21.5);
            } else if (comp instanceof LegacyDeviceAdapter legacyAc) {
                legacyAc.setCoolingIntensity(4);
            } else if (comp instanceof RoomComposite room) {
                applyRecursive(room.getChildren());
            }
        }
    }
}
