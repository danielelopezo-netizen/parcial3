package co.edu.uniquindio.smarthome.patterns.behavioral.strategy;

import co.edu.uniquindio.smarthome.model.devices.SmartLight;
import co.edu.uniquindio.smarthome.model.devices.SmartThermostat;
import co.edu.uniquindio.smarthome.patterns.structural.composite.RoomComposite;
import co.edu.uniquindio.smarthome.patterns.structural.composite.SmartComponent;

import java.util.List;

/**
 * Patrón de Comportamiento: Strategy
 * Interfaz que define el algoritmo intercambiable de optimización y gestión energética.
 */
public interface IEnergyStrategy {
    String getName();
    String getDescription();
    String getIcon();
    double getEstimatedSavingsPercentage();

    void apply(List<SmartComponent> components);
}
