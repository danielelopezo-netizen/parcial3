package co.edu.uniquindio.smarthome.patterns.behavioral.state;

/**
 * Estado: ARMADO EN CASA (Stay / Night Mode)
 * Los residentes están dentro durmiendo. Se vigilan puertas y perímetro exterior,
 * pero no el movimiento en pasillos internos.
 */
public class ArmedHomeState implements ISecurityState {

    @Override
    public String getStateName() {
        return "ARMADO (EN CASA)";
    }

    @Override
    public String getDescription() {
        return "Perímetro vigilado y puertas aseguradas. Sensores interiores en modo silencioso.";
    }

    @Override
    public String getBadgeColorHex() {
        return "#F39C12"; // Naranja
    }

    @Override
    public void armHome(SecurityContext context) {
        // Ya se encuentra en este estado
    }

    @Override
    public void armAway(SecurityContext context) {
        context.setState(new ArmedAwayState());
        context.publishSecurityEvent("Cambio de seguridad a 'FUERA DE CASA'", "WARNING");
    }

    @Override
    public void disarm(SecurityContext context, String pin) {
        if (context.validatePin(pin)) {
            context.setState(new DisarmedState());
            context.publishSecurityEvent("Sistema desarmado exitosamente con PIN", "INFO");
        } else {
            context.publishSecurityEvent("Intento de desarme fallido: PIN incorrecto", "WARNING");
        }
    }

    @Override
    public void handleMotion(SecurityContext context, String zone) {
        // Movimiento interior permitido en modo Home, solo se registra
        context.publishSecurityEvent("Movimiento interno nocturno en: " + zone, "INFO");
    }

    @Override
    public void handleDoorSensor(SecurityContext context, String doorName) {
        // Puerta exterior abierta en la noche activa alarma
        context.setState(new AlarmTriggeredState("¡Intrusión perimetral en " + doorName + "!"));
        context.publishSecurityEvent("¡ALERTA PERIMETRAL! Apertura no autorizada en " + doorName, "DANGER");
    }

    @Override
    public void handleSmokeHazard(SecurityContext context, String location) {
        context.setState(new AlarmTriggeredState("¡INCENDIO/HUMO en " + location + "!"));
        context.publishSecurityEvent("¡EMERGENCIA! Detección de humo en " + location, "DANGER");
    }
}
