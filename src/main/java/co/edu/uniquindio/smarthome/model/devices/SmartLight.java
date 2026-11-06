package co.edu.uniquindio.smarthome.model.devices;

import co.edu.uniquindio.smarthome.model.DeviceType;
import co.edu.uniquindio.smarthome.model.SmartDevice;

public class SmartLight extends SmartDevice {
    private int brightnessPercentage; // 0 a 100
    private String colorHex;

    public SmartLight(String id, String name, String roomLocation, double basePowerWatts) {
        super(id, name, DeviceType.LIGHT, roomLocation, basePowerWatts);
        this.brightnessPercentage = 80;
        this.colorHex = "#FFEAA7"; // Luz cálida por defecto
    }

    public int getBrightnessPercentage() {
        return brightnessPercentage;
    }

    public void setBrightnessPercentage(int brightnessPercentage) {
        this.brightnessPercentage = Math.max(0, Math.min(100, brightnessPercentage));
        if (this.brightnessPercentage > 0 && !this.on) {
            this.on = true;
        } else if (this.brightnessPercentage == 0) {
            this.on = false;
        }
    }

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    @Override
    public double getPowerConsumptionWatts() {
        if (!on) return 0.2;
        return (basePowerWatts * (brightnessPercentage / 100.0));
    }

    @Override
    public String getSpecificStatusDescription() {
        if (!on) return "Apagada";
        return "Brillo: " + brightnessPercentage + "% | Color: " + colorHex;
    }
}
