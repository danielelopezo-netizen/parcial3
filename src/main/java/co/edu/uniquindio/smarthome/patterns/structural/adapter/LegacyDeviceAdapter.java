package co.edu.uniquindio.smarthome.patterns.structural.adapter;

import co.edu.uniquindio.smarthome.model.DeviceType;
import co.edu.uniquindio.smarthome.model.SmartDevice;

/**
 * Patrón Estructural: Adapter
 * Adapta el electrodoméstico antiguo {@link LegacyAirConditioner}
 * a la arquitectura estándar {@link SmartDevice}.
 */
public class LegacyDeviceAdapter extends SmartDevice {
    private final LegacyAirConditioner adaptee;

    public LegacyDeviceAdapter(String id, String name, LegacyAirConditioner adaptee) {
        super(id, name, DeviceType.LEGACY_ADAPTER, adaptee.getLocationName(), 800.0);
        this.adaptee = adaptee;
        this.on = adaptee.queryPowerPulse() == 1;
    }

    @Override
    public void turnOn() {
        adaptee.sendPowerPulse(1);
        this.on = true;
    }

    @Override
    public void turnOff() {
        adaptee.sendPowerPulse(0);
        this.on = false;
    }

    @Override
    public boolean isOn() {
        return adaptee.queryPowerPulse() == 1;
    }

    @Override
    public double getPowerConsumptionWatts() {
        return adaptee.measureAnalogWattDraw();
    }

    public void setCoolingIntensity(int level) {
        adaptee.adjustCoolingIntensity(level);
    }

    public int getCoolingIntensity() {
        return adaptee.getCoolingIntensity();
    }

    public LegacyAirConditioner getAdaptee() {
        return adaptee;
    }

    @Override
    public String getSpecificStatusDescription() {
        return String.format("A/C Clásico Adaptado [Serial: %s] | Nivel: %d/5 | Consumo: %.1f W",
                adaptee.getSerialCode(), adaptee.getCoolingIntensity(), adaptee.measureAnalogWattDraw());
    }
}
