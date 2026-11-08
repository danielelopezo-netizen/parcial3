package co.edu.uniquindio.smarthome.patterns.behavioral.mediator;

import co.edu.uniquindio.smarthome.model.DeviceType;
import co.edu.uniquindio.smarthome.model.SmartDevice;
import co.edu.uniquindio.smarthome.model.devices.SmartLight;
import co.edu.uniquindio.smarthome.model.devices.SmartLock;
import co.edu.uniquindio.smarthome.patterns.behavioral.observer.EventType;
import co.edu.uniquindio.smarthome.patterns.behavioral.observer.ISubject;
import co.edu.uniquindio.smarthome.patterns.behavioral.observer.SmartHomeEvent;
import co.edu.uniquindio.smarthome.patterns.behavioral.state.SecurityContext;
import co.edu.uniquindio.smarthome.patterns.structural.composite.RoomComposite;
import co.edu.uniquindio.smarthome.patterns.structural.composite.SmartComponent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementación concreta del Mediador:
 * Coordina la comunicación cruzada entre sensores, actuadores,
 * el sistema de seguridad y el bus de eventos del hogar.
 */
public class SmartHomeHubMediator implements ISmartHomeMediator {
    private final Map<String, Object> registry = new HashMap<>();
    private ISubject eventBus;
    private SecurityContext securityContext;
    private List<RoomComposite> rooms;

    public SmartHomeHubMediator(ISubject eventBus, SecurityContext securityContext, List<RoomComposite> rooms) {
        this.eventBus = eventBus;
        this.securityContext = securityContext;
        this.rooms = rooms;
    }

    @Override
    public void registerComponent(String key, Object component) {
        registry.put(key, component);
    }

    @Override
    public Object getComponent(String key) {
        return registry.get(key);
    }

    @Override
    public synchronized void notify(Object sender, String eventName, Object eventData) {
        switch (eventName) {
            case "SMOKE_DETECTED" -> handleSmokeEmergency(sender, (String) eventData);
            case "MOTION_DETECTED" -> handleMotionSignal(sender, (String) eventData);
            case "DOOR_OPENED" -> handleDoorSignal(sender, (String) eventData);
            case "SECURITY_ARMED_AWAY" -> handleArmedAwayRoutine();
            case "DEVICE_STATE_CHANGED" -> handleDeviceStateChanged((SmartComponent) sender);
            default -> {
                if (eventBus != null) {
                    eventBus.notifyObservers(new SmartHomeEvent(
                            EventType.AUTOMATION_TRIGGERED,
                            sender != null ? sender.getClass().getSimpleName() : "Hub",
                            "Evento coordinado: " + eventName,
                            "INFO"
                    ));
                }
            }
        }
    }

    private void handleSmokeEmergency(Object sender, String location) {
        // 1. Notificar al sistema de seguridad (pasa a AlarmTriggeredState)
        securityContext.onSmokeDetected(location);

        // 2. Desbloquear todas las cerraduras para evacuación rápida
        unlockAllDoorsForSafety();

        // 3. Encender luces al 100% en color de emergencia
        turnOnEmergencyLights();

        // 4. Emitir evento crítico
        eventBus.notifyObservers(new SmartHomeEvent(
                EventType.SMOKE_ALARM,
                "Mediator [Protocolo Evacuación]",
                "¡Incendio en " + location + "! Puertas desbloqueadas e iluminación de evacuación activada.",
                "DANGER"
        ));
    }

    private void handleMotionSignal(Object sender, String zone) {
        securityContext.onMotionDetected(zone);

        // Si el sistema no está en alarma y está desarmado, encender luz automáticamente por confort
        if ("DESARMADO".equalsIgnoreCase(securityContext.getState().getStateName())) {
            turnOnLightsInRoom(zone);
        }
    }

    private void handleDoorSignal(Object sender, String doorName) {
        securityContext.onDoorOpened(doorName);
    }

    private void handleArmedAwayRoutine() {
        // Bloquear todas las cerraduras
        lockAllDoors();

        eventBus.notifyObservers(new SmartHomeEvent(
                EventType.SECURITY_STATE_CHANGED,
                "Mediator [Seguridad]",
                "Modo Fuera coordinado: cerraduras aseguradas y sensores en máxima vigilancia.",
                "WARNING"
        ));
    }

    private void handleDeviceStateChanged(SmartComponent comp) {
        eventBus.notifyObservers(new SmartHomeEvent(
                EventType.DEVICE_STATE_CHANGED,
                comp.getName(),
                "Estado: " + (comp.isOn() ? "ENCENDIDO" : "APAGADO") + " | " + comp.getDisplayLocation(),
                "INFO"
        ));
    }

    private void unlockAllDoorsForSafety() {
        for (RoomComposite room : rooms) {
            for (SmartComponent c : room.getChildren()) {
                if (c instanceof SmartLock lock) {
                    lock.unlock();
                }
            }
        }
    }

    private void lockAllDoors() {
        for (RoomComposite room : rooms) {
            for (SmartComponent c : room.getChildren()) {
                if (c instanceof SmartLock lock) {
                    lock.lock();
                }
            }
        }
    }

    private void turnOnEmergencyLights() {
        for (RoomComposite room : rooms) {
            for (SmartComponent c : room.getChildren()) {
                if (c instanceof SmartLight light) {
                    light.turnOn();
                    light.setBrightnessPercentage(100);
                    light.setColorHex("#FF0033"); // Rojo de alerta
                }
            }
        }
    }

    private void turnOnLightsInRoom(String roomName) {
        for (RoomComposite room : rooms) {
            if (room.getName().equalsIgnoreCase(roomName) || roomName.contains(room.getName())) {
                for (SmartComponent c : room.getChildren()) {
                    if (c instanceof SmartLight light && !light.isOn()) {
                        light.turnOn();
                        light.setBrightnessPercentage(75);
                    }
                }
            }
        }
    }
}
