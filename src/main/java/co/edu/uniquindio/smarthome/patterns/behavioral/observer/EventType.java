package co.edu.uniquindio.smarthome.patterns.behavioral.observer;

public enum EventType {
    DEVICE_STATE_CHANGED("Estado de Dispositivo", "ℹ️"),
    MOTION_DETECTED("Movimiento Detectado", "🚶"),
    SMOKE_ALARM("Alerta de Humo/Gas", "🔥"),
    SECURITY_STATE_CHANGED("Sistema de Seguridad", "🛡️"),
    ENERGY_STRATEGY_CHANGED("Estrategia Energética", "⚡"),
    COMMAND_EXECUTED("Comando Ejecutado", "⚙️"),
    COMMAND_UNDONE("Comando Revertido (Undo)", "↩️"),
    AUTOMATION_TRIGGERED("Automatización", "🤖");

    private final String label;
    private final String icon;

    EventType(String label, String icon) {
        this.label = label;
        this.icon = icon;
    }

    public String getLabel() {
        return label;
    }

    public String getIcon() {
        return icon;
    }
}
