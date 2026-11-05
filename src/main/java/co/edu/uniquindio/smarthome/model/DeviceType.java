package co.edu.uniquindio.smarthome.model;

public enum DeviceType {
    LIGHT("Iluminación Inteligente", "💡"),
    THERMOSTAT("Termostato Climatizador", "🌡️"),
    LOCK("Cerradura Electrónica", "🔒"),
    SECURITY_CAMERA("Cámara de Seguridad", "📹"),
    MOTION_SENSOR("Sensor de Movimiento", "🚶"),
    SMOKE_SENSOR("Sensor de Humo y Gas", "🚨"),
    LEGACY_ADAPTER("Dispositivo Adaptado (Legacy)", "🔌");

    private final String displayName;
    private final String icon;

    DeviceType(String displayName, String icon) {
        this.displayName = displayName;
        this.icon = icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }
}
