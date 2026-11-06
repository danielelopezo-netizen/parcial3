package co.edu.uniquindio.smarthome.patterns.structural.adapter;

/**
 * Clase de dispositivo de terceros / protocolo antiguo (Legacy).
 * No implementa la interfaz SmartComponent ni hereda de SmartDevice.
 * Posee una interfaz incompatible con nombres y tipos distintos.
 */
public class LegacyAirConditioner {
    private final String serialCode;
    private final String locationName;
    private int powerState; // 0 = OFF, 1 = ON
    private int coolingLevel; // 1 a 5
    private double currentWattage;

    public LegacyAirConditioner(String serialCode, String locationName) {
        this.serialCode = serialCode;
        this.locationName = locationName;
        this.powerState = 0;
        this.coolingLevel = 3;
        this.currentWattage = 5.0;
    }

    public void sendPowerPulse(int state) {
        this.powerState = (state == 1) ? 1 : 0;
        this.currentWattage = (powerState == 1) ? (600.0 + (coolingLevel * 150.0)) : 4.0;
    }

    public int queryPowerPulse() {
        return this.powerState;
    }

    public void adjustCoolingIntensity(int level) {
        this.coolingLevel = Math.max(1, Math.min(5, level));
        if (powerState == 1) {
            this.currentWattage = 600.0 + (coolingLevel * 150.0);
        }
    }

    public int getCoolingIntensity() {
        return coolingLevel;
    }

    public double measureAnalogWattDraw() {
        return currentWattage;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public String getLocationName() {
        return locationName;
    }
}
