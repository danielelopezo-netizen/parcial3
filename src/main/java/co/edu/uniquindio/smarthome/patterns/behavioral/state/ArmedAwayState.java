package co.edu.uniquindio.smarthome.patterns.behavioral.state;

/**
 * Estado: ARMADO FUERA DE CASA (Away Mode)
 * Máxima vigilancia: no debería haber nadie en casa. Cualquier movimiento o puerta
 * provoca de inmediato el salto de la alarma sonora.
 */
public class ArmedAwayState implements ISecurityState {

    @Override
    public String getStateName() {
        return "ARMADO (FUERA)";
    }

    @Override
    public String getDescription() {
        return "Vigilancia máxima activa. Toda la casa monitoreada.";
    }

    @Override
    public String getBadgeColorHex() {
        return "#E67E22"; // Naranja oscuro / Rojo suave
    }

    @Override
    public void armHome(SecurityContext context) {
        context.setState(new ArmedHomeState());
        context.publishSecurityEvent("Modo de seguridad cambiado a 'EN CASA'", "INFO");
    }

    @Override
    public void armAway(SecurityContext context) {
        // Ya está en Away
    }

    @Override
    public void disarm(SecurityContext context, String pin) {
        if (context.validatePin(pin)) {
            context.setState(new DisarmedState());
            context.publishSecurityEvent("Hogar desarmado con clave de seguridad", "INFO");
        } else {
            context.publishSecurityEvent("¡Alerta! Intento de desarme no autorizado con PIN erróneo", "DANGER");
        }
    }

    @Override
    public void handleMotion(SecurityContext context, String zone) {
        context.setState(new AlarmTriggeredState("¡Intruso detectado por movimiento en " + zone + "!"));
        context.publishSecurityEvent("¡ALARMA DE INTRUSIÓN! Movimiento no esperado en " + zone, "DANGER");
    }

    @Override
    public void handleDoorSensor(SecurityContext context, String doorName) {
        context.setState(new AlarmTriggeredState("¡Acceso forzado en " + doorName + "!"));
        context.publishSecurityEvent("¡ALARMA DE ACCESO! Puerta violentada: " + doorName, "DANGER");
    }

    @Override
    public void handleSmokeHazard(SecurityContext context, String location) {
        context.setState(new AlarmTriggeredState("¡INCENDIO/HUMO en " + location + "!"));
        context.publishSecurityEvent("¡INCENDIO DETECTADO! Activación inmediata en " + location, "DANGER");
    }
}
