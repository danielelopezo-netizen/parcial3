package co.edu.uniquindio.smarthome.patterns.behavioral.observer;

/**
 * Patrón de Comportamiento: Observer
 * Interfaz de los observadores suscritos a eventos del hogar inteligente.
 */
public interface IObserver {
    void onEvent(SmartHomeEvent event);
}
