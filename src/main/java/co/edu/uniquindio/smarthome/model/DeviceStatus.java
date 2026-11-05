package co.edu.uniquindio.smarthome.model;

public enum DeviceStatus {
    ONLINE("En Línea"),
    OFFLINE("Desconectado"),
    ALERT("En Alerta"),
    FAULT("Falla de Conexión");

    private final String description;

    DeviceStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
