package co.edu.uniquindio.smarthome.patterns.behavioral.state;

import co.edu.uniquindio.smarthome.patterns.behavioral.observer.EventType;
import co.edu.uniquindio.smarthome.patterns.behavioral.observer.SmartHomeEvent;

import java.util.function.BiConsumer;

/**
 * Contexto del patrón State: Mantiene la referencia al estado actual
 * y delega las operaciones al objeto estado concreto.
 */
public class SecurityContext {
    private ISecurityState currentState;
    private String systemPin;
    private BiConsumer<String, String> eventBroadcaster; // (mensaje, severidad)

    public SecurityContext(String systemPin) {
        this.systemPin = systemPin;
        this.currentState = new DisarmedState();
    }

    public void setEventBroadcaster(BiConsumer<String, String> eventBroadcaster) {
        this.eventBroadcaster = eventBroadcaster;
    }

    public synchronized ISecurityState getState() {
        return currentState;
    }

    public synchronized void setState(ISecurityState newState) {
        this.currentState = newState;
    }

    public boolean validatePin(String pin) {
        return systemPin != null && systemPin.equals(pin);
    }

    public void setSystemPin(String systemPin) {
        this.systemPin = systemPin;
    }

    public void armHome() {
        currentState.armHome(this);
    }

    public void armAway() {
        currentState.armAway(this);
    }

    public void disarm(String pin) {
        currentState.disarm(this, pin);
    }

    public void onMotionDetected(String zone) {
        currentState.handleMotion(this, zone);
    }

    public void onDoorOpened(String doorName) {
        currentState.handleDoorSensor(this, doorName);
    }

    public void onSmokeDetected(String location) {
        currentState.handleSmokeHazard(this, location);
    }

    public void publishSecurityEvent(String description, String severity) {
        if (eventBroadcaster != null) {
            eventBroadcaster.accept(description, severity);
        }
    }
}
