package co.edu.uniquindio.smarthome.patterns.behavioral.command;

import co.edu.uniquindio.smarthome.model.devices.SmartThermostat;

public class SetTemperatureCommand implements ICommand {
    private final SmartThermostat thermostat;
    private final double newTemperature;
    private double previousTemperature;

    public SetTemperatureCommand(SmartThermostat thermostat, double newTemperature) {
        this.thermostat = thermostat;
        this.newTemperature = newTemperature;
    }

    @Override
    public void execute() {
        this.previousTemperature = thermostat.getTargetTemperature();
        thermostat.setTargetTemperature(newTemperature);
    }

    @Override
    public void undo() {
        thermostat.setTargetTemperature(previousTemperature);
    }

    @Override
    public String getDescription() {
        return String.format("Ajustar Termostato '%s' a %.1f°C (antes %.1f°C)",
                thermostat.getName(), newTemperature, previousTemperature);
    }
}
