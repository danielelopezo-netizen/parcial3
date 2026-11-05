package co.edu.uniquindio.smarthome.patterns.structural.composite;

/**
 * Patrón Estructural: Composite
 * Componente base que define la interfaz uniforme tanto para hojas (SmartDevice)
 * como para nodos compuestos (RoomComposite / ZoneGroup).
 */
public interface SmartComponent {
    String getId();
    String getName();
    void turnOn();
    void turnOff();
    boolean isOn();
    double getPowerConsumptionWatts();
    int getComponentCount();
    String getDisplayLocation();
}
