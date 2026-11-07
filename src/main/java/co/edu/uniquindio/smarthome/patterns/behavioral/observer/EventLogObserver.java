package co.edu.uniquindio.smarthome.patterns.behavioral.observer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Observador que mantiene el historial en memoria de los eventos ocurridos
 * para ser visualizados reactivamente en la tabla o lista de la interfaz gráfica.
 */
public class EventLogObserver implements IObserver {
    private final ObservableList<SmartHomeEvent> eventHistory;
    private static final int MAX_ENTRIES = 100;

    public EventLogObserver() {
        this.eventHistory = FXCollections.observableArrayList();
    }

    @Override
    public synchronized void onEvent(SmartHomeEvent event) {
        eventHistory.add(0, event); // Agregar al inicio para ver los más recientes
        if (eventHistory.size() > MAX_ENTRIES) {
            eventHistory.remove(eventHistory.size() - 1);
        }
    }

    public ObservableList<SmartHomeEvent> getEventHistory() {
        return eventHistory;
    }

    public void clear() {
        eventHistory.clear();
    }
}
