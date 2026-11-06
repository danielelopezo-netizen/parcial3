package co.edu.uniquindio.smarthome.patterns.structural.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Patrón Estructural: Composite
 * Representa una habitación, piso o zona completa del hogar inteligente.
 * Puede contener hojas (SmartDevice) u otros compuestos (sub-habitaciones / sub-zonas),
 * permitiendo control masivo y agregación transparente de consumo eléctrico.
 */
public class RoomComposite implements SmartComponent {
    private final String id;
    private final String name;
    private final String locationDescription;
    private final List<SmartComponent> children;

    public RoomComposite(String id, String name, String locationDescription) {
        this.id = id;
        this.name = name;
        this.locationDescription = locationDescription;
        this.children = new ArrayList<>();
    }

    public void addComponent(SmartComponent component) {
        if (component != null && !children.contains(component)) {
            children.add(component);
        }
    }

    public void removeComponent(SmartComponent component) {
        children.remove(component);
    }

    public List<SmartComponent> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDisplayLocation() {
        return locationDescription;
    }

    @Override
    public void turnOn() {
        for (SmartComponent component : children) {
            component.turnOn();
        }
    }

    @Override
    public void turnOff() {
        for (SmartComponent component : children) {
            component.turnOff();
        }
    }

    @Override
    public boolean isOn() {
        // Se considera encendido si al menos uno de sus componentes está activo
        return children.stream().anyMatch(SmartComponent::isOn);
    }

    @Override
    public double getPowerConsumptionWatts() {
        // Suma polimórfica de la potencia consumida por todos los hijos (hojas y compuestos)
        return children.stream()
                .mapToDouble(SmartComponent::getPowerConsumptionWatts)
                .sum();
    }

    @Override
    public int getComponentCount() {
        return children.stream()
                .mapToInt(SmartComponent::getComponentCount)
                .sum();
    }

    @Override
    public String toString() {
        return "🏠 " + name + " (" + getComponentCount() + " dispositivos)";
    }
}
