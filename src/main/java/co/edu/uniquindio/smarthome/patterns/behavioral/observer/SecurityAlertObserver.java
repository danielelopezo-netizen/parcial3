package co.edu.uniquindio.smarthome.patterns.behavioral.observer;

import java.util.function.Consumer;

/**
 * Observador especializado en monitorear eventos de seguridad de alta prioridad
 * (humo, fuego, intrusión, violaciones perimetrales).
 */
public class SecurityAlertObserver implements IObserver {
    private Consumer<SmartHomeEvent> alertCallback;

    public SecurityAlertObserver() {
    }

    public void setAlertCallback(Consumer<SmartHomeEvent> alertCallback) {
        this.alertCallback = alertCallback;
    }

    @Override
    public void onEvent(SmartHomeEvent event) {
        if ("DANGER".equalsIgnoreCase(event.getSeverity()) ||
            event.getType() == EventType.SMOKE_ALARM ||
            event.getType() == EventType.SECURITY_STATE_CHANGED) {
            
            if (alertCallback != null) {
                alertCallback.accept(event);
            }
        }
    }
}
