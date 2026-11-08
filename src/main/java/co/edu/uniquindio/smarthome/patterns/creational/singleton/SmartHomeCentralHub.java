package co.edu.uniquindio.smarthome.patterns.creational.singleton;

import co.edu.uniquindio.smarthome.model.DeviceType;
import co.edu.uniquindio.smarthome.model.SmartDevice;
import co.edu.uniquindio.smarthome.model.devices.SmartLock;
import co.edu.uniquindio.smarthome.model.devices.SmartThermostat;
import co.edu.uniquindio.smarthome.patterns.behavioral.command.*;
import co.edu.uniquindio.smarthome.patterns.behavioral.mediator.ISmartHomeMediator;
import co.edu.uniquindio.smarthome.patterns.behavioral.mediator.SmartHomeHubMediator;
import co.edu.uniquindio.smarthome.patterns.behavioral.observer.*;
import co.edu.uniquindio.smarthome.patterns.behavioral.state.SecurityContext;
import co.edu.uniquindio.smarthome.patterns.behavioral.strategy.*;
import co.edu.uniquindio.smarthome.patterns.structural.composite.RoomComposite;
import co.edu.uniquindio.smarthome.patterns.structural.composite.SmartComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Patrón Creacional: Singleton
 * Punto único de acceso y administración del ecosistema del Hogar Inteligente.
 * Integra todos los patrones de diseño (Composite, Mediator, Observer, Strategy,
 * State, Command, Factory Method y Adapter).
 */
public class SmartHomeCentralHub implements ISubject {
    private static volatile SmartHomeCentralHub instance;

    // Componentes del sistema
    private final List<RoomComposite> rooms;
    private final List<IObserver> observers;
    private final EventLogObserver eventLogObserver;
    private final SecurityAlertObserver securityAlertObserver;
    private final SecurityContext securityContext;
    private final CommandInvoker commandInvoker;
    private final List<MacroSceneCommand> availableScenes;

    private IEnergyStrategy currentEnergyStrategy;
    private ISmartHomeMediator mediator;

    private SmartHomeCentralHub() {
        this.rooms = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.eventLogObserver = new EventLogObserver();
        this.securityAlertObserver = new SecurityAlertObserver();
        this.availableScenes = new ArrayList<>();
        this.commandInvoker = new CommandInvoker();

        // Registrar observadores base
        registerObserver(eventLogObserver);
        registerObserver(securityAlertObserver);

        // Inicializar contexto de seguridad con PIN por defecto "1234"
        this.securityContext = new SecurityContext("1234");
        this.securityContext.setEventBroadcaster((desc, severity) -> {
            notifyObservers(new SmartHomeEvent(EventType.SECURITY_STATE_CHANGED, "Sistema de Seguridad", desc, severity));
        });

        // Estrategia energética inicial
        this.currentEnergyStrategy = new EcoSaverStrategy();

        // Inicializar el mediador
        this.mediator = new SmartHomeHubMediator(this, this.securityContext, this.rooms);

        // Notificar al ejecutar comandos
        this.commandInvoker.setOnCommandExecutedCallback(cmd -> {
            notifyObservers(new SmartHomeEvent(
                    EventType.COMMAND_EXECUTED,
                    "Invocador de Comandos",
                    cmd.getDescription(),
                    "INFO"
            ));
        });
    }

    public static SmartHomeCentralHub getInstance() {
        if (instance == null) {
            synchronized (SmartHomeCentralHub.class) {
                if (instance == null) {
                    instance = new SmartHomeCentralHub();
                }
            }
        }
        return instance;
    }

    // --- Observer implementation ---
    @Override
    public synchronized void registerObserver(IObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public synchronized void removeObserver(IObserver observer) {
        observers.remove(observer);
    }

    @Override
    public synchronized void notifyObservers(SmartHomeEvent event) {
        for (IObserver observer : observers) {
            observer.onEvent(event);
        }
    }

    // --- Métodos de gestión de Habitaciones / Composite ---
    public void addRoom(RoomComposite room) {
        if (room != null && !rooms.contains(room)) {
            rooms.add(room);
        }
    }

    public List<RoomComposite> getRooms() {
        return Collections.unmodifiableList(rooms);
    }

    public List<SmartComponent> getAllComponentsFlat() {
        List<SmartComponent> flat = new ArrayList<>();
        for (RoomComposite room : rooms) {
            flat.add(room);
            flat.addAll(room.getChildren());
        }
        return flat;
    }

    public double getTotalPowerConsumptionWatts() {
        return rooms.stream().mapToDouble(RoomComposite::getPowerConsumptionWatts).sum();
    }

    public int getTotalDeviceCount() {
        return rooms.stream().mapToInt(RoomComposite::getComponentCount).sum();
    }

    public int getActiveDeviceCount() {
        int count = 0;
        for (RoomComposite room : rooms) {
            for (SmartComponent c : room.getChildren()) {
                if (c.isOn()) count++;
            }
        }
        return count;
    }

    // --- Strategy Pattern ---
    public IEnergyStrategy getCurrentEnergyStrategy() {
        return currentEnergyStrategy;
    }

    public void setEnergyStrategy(IEnergyStrategy strategy) {
        this.currentEnergyStrategy = strategy;
        List<SmartComponent> targetList = new ArrayList<>(rooms);
        strategy.apply(targetList);

        notifyObservers(new SmartHomeEvent(
                EventType.ENERGY_STRATEGY_CHANGED,
                "Gestor de Energía",
                "Estrategia cambiada a: " + strategy.getName() + " (" + strategy.getDescription() + ")",
                "SUCCESS"
        ));
    }

    // --- Command Pattern ---
    public CommandInvoker getCommandInvoker() {
        return commandInvoker;
    }

    public void executeCommand(ICommand command) {
        commandInvoker.executeCommand(command);
    }

    public void undoCommand() {
        ICommand cmd = commandInvoker.undo();
        if (cmd != null) {
            notifyObservers(new SmartHomeEvent(
                    EventType.COMMAND_UNDONE,
                    "Invocador de Comandos",
                    "Se deshizo la acción: " + cmd.getDescription(),
                    "WARNING"
            ));
        }
    }

    public void redoCommand() {
        ICommand cmd = commandInvoker.redo();
        if (cmd != null) {
            notifyObservers(new SmartHomeEvent(
                    EventType.COMMAND_EXECUTED,
                    "Invocador de Comandos",
                    "Se rehizo la acción: " + cmd.getDescription(),
                    "INFO"
            ));
        }
    }

    public List<MacroSceneCommand> getAvailableScenes() {
        return availableScenes;
    }

    public void addScene(MacroSceneCommand scene) {
        availableScenes.add(scene);
    }

    // --- State & Mediator ---
    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    public ISmartHomeMediator getMediator() {
        return mediator;
    }

    public EventLogObserver getEventLogObserver() {
        return eventLogObserver;
    }

    public SecurityAlertObserver getSecurityAlertObserver() {
        return securityAlertObserver;
    }
}
