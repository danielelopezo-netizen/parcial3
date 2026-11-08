package co.edu.uniquindio.smarthome.patterns.behavioral.mediator;

/**
 * Patrón de Comportamiento: Mediator
 * Define la interfaz para el mediador central que coordina la interacción
 * entre los diferentes subsistemas sin acoplamiento directo entre ellos.
 */
public interface ISmartHomeMediator {
    void notify(Object sender, String eventName, Object eventData);
    void registerComponent(String key, Object component);
    Object getComponent(String key);
}
