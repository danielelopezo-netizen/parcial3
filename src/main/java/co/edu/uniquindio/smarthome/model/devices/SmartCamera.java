package co.edu.uniquindio.smarthome.model.devices;

import co.edu.uniquindio.smarthome.model.DeviceType;
import co.edu.uniquindio.smarthome.model.SmartDevice;

public class SmartCamera extends SmartDevice {
    private boolean recording;
    private boolean nightVision;
    private String streamUrl;

    public SmartCamera(String id, String name, String roomLocation, double basePowerWatts) {
        super(id, name, DeviceType.SECURITY_CAMERA, roomLocation, basePowerWatts);
        this.recording = true;
        this.nightVision = true;
        this.streamUrl = "rtsp://camera." + id + ".local/live";
        this.on = true;
    }

    public boolean isRecording() {
        return recording;
    }

    public void setRecording(boolean recording) {
        this.recording = recording;
    }

    public boolean isNightVision() {
        return nightVision;
    }

    public void setNightVision(boolean nightVision) {
        this.nightVision = nightVision;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    @Override
    public double getPowerConsumptionWatts() {
        if (!on) return 0.5;
        return recording ? basePowerWatts : (basePowerWatts * 0.4);
    }

    @Override
    public String getSpecificStatusDescription() {
        if (!on) return "Cámara Standby / Apagada";
        return (recording ? "Grabando 1080p 📹" : "En Espera") + (nightVision ? " | Visión Nocturna ON" : "");
    }
}
