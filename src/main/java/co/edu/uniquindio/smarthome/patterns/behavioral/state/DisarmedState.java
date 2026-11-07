package co.edu.uniquindio.smarthome.patterns.behavioral.state;

import co.edu.uniquindio.smarthome.patterns.behavioral.observer.EventType;
import co.edu.uniquindio.smarthome.patterns.behavioral.observer.SmartHomeEvent;

/**
 * Estado: DESARMADO
 * El sistema está en modo cotidiano. No se generan alertas por movimiento.
 */
public class DisarmedState implements ISecurityState {

    @Override
    public String getStateName() {
        return "DESARMADO";
    }

    @Override
    public String getDescription() {
        return "Seguridad desactivada. Tránsito libre por el hogar.";
    }

    @Override
    public String getBadgeColorHex() {
        return "#2ECC71"; // Verde
    }

    @Override
    public void armHome(SecurityContext context) {
        context.setState(new ArmedHomeState());
        context.publishSecurityEvent("Sistema armado en modo 'EN CASA' (Perímetro vigilado)", "INFO");
    }

    @Override
    public void armAway(SecurityContext context) {
        context.setState(new ArmedAwayState());
        context.publishSecurityEvent("Sistema armado en modo 'FUERA DE CASA' (Protección total)", "WARNING");
    }

    @Override
    public void disarm(SecurityContext context, String pin) {
        // Ya está desarmado
    }

    @Override
    public void handleMotion(SecurityContext context, String zone) {
        context.publishSecurityEvent("Movimiento rutinario registrado en: " + zone, "INFO");
    }

    @Override
    public void handleDoorSensor(SecurityContext context, String doorName) {
        context.publishSecurityEvent("Apertura autorizada de: " + doorName, "INFO");
    }

    @Override
    public void handleSmokeHazard(SecurityContext context, String location) {
        // El humo siempre dispara la alarma incluso si el sistema está desarmado
        context.setState(new AlarmTriggeredState("¡INCENDIO/HUMO en " + location + "!"));
        context.publishSecurityEvent("¡PELIGRO! Sensor de humo activó la alarma en " + location, "DANGER");
    }
}
