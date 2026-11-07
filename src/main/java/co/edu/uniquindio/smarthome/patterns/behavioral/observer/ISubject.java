package co.edu.uniquindio.smarthome.patterns.behavioral.observer;

/**
 * Patrón de Comportamiento: Observer
 * Sujeto observable que administra suscripciones y despacho de notificaciones.
 */
public interface ISubject {
    void registerObserver(IObserver observer);
    void removeObserver(IObserver observer);
    void notifyObservers(SmartHomeEvent event);
}
