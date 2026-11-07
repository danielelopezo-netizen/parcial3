package co.edu.uniquindio.smarthome.patterns.behavioral.state;

/**
 * Estado: ALARMA DISPARADA (Sirena activa)
 * Ocurre ante una intrusión o peligro de incendio.
 * Permanece en alerta hasta que se ingrese el PIN de desarme.
 */
public class AlarmTriggeredState implements ISecurityState {
    private final String reason;

    public AlarmTriggeredState(String reason) {
        this.reason = reason;
    }

    @Override
    public String getStateName() {
        return "¡ALARMA ACTIVADA!";
    }

    @Override
    public String getDescription() {
        return "SIRENA SONANDO. Causa: " + reason;
    }

    @Override
    public String getBadgeColorHex() {
        return "#E74C3C"; // Rojo brillante
    }

    @Override
    public void armHome(SecurityContext context) {
        // En alarma no se puede armar sin desarmar antes con PIN
        context.publishSecurityEvent("No es posible cambiar de modo mientras la sirena está activada. Ingrese el PIN.", "WARNING");
    }

    @Override
    public void armAway(SecurityContext context) {
        context.publishSecurityEvent("La alarma ya está sonando.", "WARNING");
    }

    @Override
    public void disarm(SecurityContext context, String pin) {
        if (context.validatePin(pin)) {
            context.setState(new DisarmedState());
            context.publishSecurityEvent("Sirena silenciada y sistema DESARMADO mediante clave de emergencia.", "SUCCESS");
        } else {
            context.publishSecurityEvent("PIN INVÁLIDO. La alarma continúa sonando.", "DANGER");
        }
    }

    @Override
    public void handleMotion(SecurityContext context, String zone) {
        context.publishSecurityEvent("Alerta repetida de movimiento en: " + zone, "DANGER");
    }

    @Override
    public void handleDoorSensor(SecurityContext context, String doorName) {
        context.publishSecurityEvent("Sensor de puerta activado durante alarma: " + doorName, "DANGER");
    }

    @Override
    public void handleSmokeHazard(SecurityContext context, String location) {
        context.publishSecurityEvent("Alerta crítica de humo persistente en " + location, "DANGER");
    }
}
