package co.edu.uniquindio.smarthome.patterns.behavioral.state;

/**
 * Patrón de Comportamiento: State
 * Define la interfaz para los distintos estados operativos del sistema de seguridad.
 */
public interface ISecurityState {
    String getStateName();
    String getDescription();
    String getBadgeColorHex();

    void armHome(SecurityContext context);
    void armAway(SecurityContext context);
    void disarm(SecurityContext context, String pin);
    void handleMotion(SecurityContext context, String zone);
    void handleDoorSensor(SecurityContext context, String doorName);
    void handleSmokeHazard(SecurityContext context, String location);
}
