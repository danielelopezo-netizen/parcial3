package co.edu.uniquindio.smarthome;

import co.edu.uniquindio.smarthome.model.DeviceType;
import co.edu.uniquindio.smarthome.model.SmartDevice;
import co.edu.uniquindio.smarthome.model.devices.SmartLight;
import co.edu.uniquindio.smarthome.model.devices.SmartLock;
import co.edu.uniquindio.smarthome.model.devices.SmartThermostat;
import co.edu.uniquindio.smarthome.patterns.behavioral.command.*;
import co.edu.uniquindio.smarthome.patterns.behavioral.mediator.SmartHomeHubMediator;
import co.edu.uniquindio.smarthome.patterns.behavioral.observer.EventLogObserver;
import co.edu.uniquindio.smarthome.patterns.behavioral.observer.EventType;
import co.edu.uniquindio.smarthome.patterns.behavioral.observer.SmartHomeEvent;
import co.edu.uniquindio.smarthome.patterns.behavioral.state.*;
import co.edu.uniquindio.smarthome.patterns.behavioral.strategy.*;
import co.edu.uniquindio.smarthome.patterns.creational.factory.DeviceFactory;
import co.edu.uniquindio.smarthome.patterns.creational.singleton.SmartHomeCentralHub;
import co.edu.uniquindio.smarthome.patterns.structural.adapter.LegacyAirConditioner;
import co.edu.uniquindio.smarthome.patterns.structural.adapter.LegacyDeviceAdapter;
import co.edu.uniquindio.smarthome.patterns.structural.composite.RoomComposite;
import co.edu.uniquindio.smarthome.patterns.structural.composite.SmartComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PatternsTest {

    private SmartHomeCentralHub hub;

    @BeforeEach
    public void setUp() {
        hub = SmartHomeCentralHub.getInstance();
    }

    @Test
    @DisplayName("Patrón Creacional: Factory Method - Creación correcta de dispositivos polimórficos")
    public void testFactoryMethod() {
        SmartDevice light = DeviceFactory.createDevice(DeviceType.LIGHT, "T-01", "Luz Test", "Sala");
        SmartDevice thermo = DeviceFactory.createDevice(DeviceType.THERMOSTAT, "T-02", "Termostato Test", "Sala");
        SmartDevice lock = DeviceFactory.createDevice(DeviceType.LOCK, "T-03", "Cerradura Test", "Entrada");

        assertInstanceOf(SmartLight.class, light);
        assertInstanceOf(SmartThermostat.class, thermo);
        assertInstanceOf(SmartLock.class, lock);
        assertEquals("Luz Test", light.getName());
    }

    @Test
    @DisplayName("Patrón Estructural: Adapter - Integración de dispositivo antiguo incompatible")
    public void testAdapterPattern() {
        LegacyAirConditioner legacyAc = new LegacyAirConditioner("SN-OLD-1995", "Cocina");
        LegacyDeviceAdapter adapter = new LegacyDeviceAdapter("ADAPT-01", "AC Adaptado", legacyAc);

        assertFalse(adapter.isOn());
        adapter.turnOn();
        assertTrue(adapter.isOn());
        assertEquals(1, legacyAc.queryPowerPulse());
        assertTrue(adapter.getPowerConsumptionWatts() > 0);

        adapter.turnOff();
        assertFalse(adapter.isOn());
        assertEquals(0, legacyAc.queryPowerPulse());
    }

    @Test
    @DisplayName("Patrón Estructural: Composite - Manejo uniforme de hojas y compuestos")
    public void testCompositePattern() {
        RoomComposite room = new RoomComposite("R-01", "Estudio", "Planta Alta");
        SmartDevice light = DeviceFactory.createDevice(DeviceType.LIGHT, "L-01", "Luz 1", "Estudio");
        SmartDevice light2 = DeviceFactory.createDevice(DeviceType.LIGHT, "L-02", "Luz 2", "Estudio");

        room.addComponent(light);
        room.addComponent(light2);

        assertEquals(2, room.getComponentCount());
        assertFalse(room.isOn());

        // Encender toda la habitación polimórficamente
        room.turnOn();
        assertTrue(room.isOn());
        assertTrue(light.isOn());
        assertTrue(light2.isOn());

        // Apagar toda la habitación
        room.turnOff();
        assertFalse(room.isOn());
        assertFalse(light.isOn());
    }

    @Test
    @DisplayName("Patrón Comportamiento: Command - Ejecución, Deshacer (Undo) y Rehacer (Redo)")
    public void testCommandPatternUndoRedo() {
        SmartDevice light = DeviceFactory.createDevice(DeviceType.LIGHT, "L-CMD", "Luz Comedor", "Comedor");
        CommandInvoker invoker = new CommandInvoker();

        assertFalse(light.isOn());

        // Ejecutar TurnOnCommand
        ICommand turnOn = new TurnOnCommand(light);
        invoker.executeCommand(turnOn);
        assertTrue(light.isOn());
        assertTrue(invoker.canUndo());

        // Deshacer
        invoker.undo();
        assertFalse(light.isOn());
        assertTrue(invoker.canRedo());

        // Rehacer
        invoker.redo();
        assertTrue(light.isOn());
    }

    @Test
    @DisplayName("Patrón Comportamiento: State - Transiciones y comportamiento dinámico de seguridad")
    public void testStatePatternTransitions() {
        SecurityContext sec = new SecurityContext("1234");
        assertInstanceOf(DisarmedState.class, sec.getState());

        // Armar Modo Fuera
        sec.armAway();
        assertInstanceOf(ArmedAwayState.class, sec.getState());

        // Detección de intrusión dispara alarma
        sec.onMotionDetected("Patio Trasero");
        assertInstanceOf(AlarmTriggeredState.class, sec.getState());

        // Desarme con PIN incorrecto no silencia alarma
        sec.disarm("9999");
        assertInstanceOf(AlarmTriggeredState.class, sec.getState());

        // Desarme con PIN correcto vuelve a Desarmado
        sec.disarm("1234");
        assertInstanceOf(DisarmedState.class, sec.getState());
    }

    @Test
    @DisplayName("Patrón Comportamiento: Strategy - Algoritmos de optimización de energía intercambiables")
    public void testStrategyPattern() {
        SmartLight light = (SmartLight) DeviceFactory.createDevice(DeviceType.LIGHT, "L-STRAT", "Luz Sala", "Sala");
        SmartThermostat thermo = (SmartThermostat) DeviceFactory.createDevice(DeviceType.THERMOSTAT, "T-STRAT", "Clima Sala", "Sala");
        light.turnOn();

        List<SmartComponent> comps = List.of(light, thermo);

        // Aplicar Modo Confort
        IEnergyStrategy comfort = new ComfortPriorityStrategy();
        comfort.apply(comps);
        assertEquals(90, light.getBrightnessPercentage());
        assertEquals(21.5, thermo.getTargetTemperature(), 0.01);

        // Aplicar Modo Eco
        IEnergyStrategy eco = new EcoSaverStrategy();
        eco.apply(comps);
        assertEquals(50, light.getBrightnessPercentage());
        assertEquals(24.5, thermo.getTargetTemperature(), 0.01);
    }

    @Test
    @DisplayName("Patrón Comportamiento: Observer - Despacho reactivo de eventos")
    public void testObserverPattern() {
        EventLogObserver logObserver = new EventLogObserver();
        hub.registerObserver(logObserver);

        int initialSize = logObserver.getEventHistory().size();
        hub.notifyObservers(new SmartHomeEvent(EventType.DEVICE_STATE_CHANGED, "SensorTest", "Evento de prueba", "INFO"));

        assertEquals(initialSize + 1, logObserver.getEventHistory().size());
    }

    @Test
    @DisplayName("Patrón Comportamiento: Mediator - Coordinación cruzada ante emergencia de humo")
    public void testMediatorPatternSmokeEmergency() {
        RoomComposite room = new RoomComposite("R-MED", "Cocina", "Planta Baja");
        SmartLock door = (SmartLock) DeviceFactory.createDevice(DeviceType.LOCK, "LOCK-01", "Puerta Escape", "Cocina");
        door.lock();
        assertTrue(door.isLocked());

        room.addComponent(door);
        hub.addRoom(room);

        // Mediador notifica detección de humo
        hub.getMediator().notify(this, "SMOKE_DETECTED", "Cocina");

        // El mediador debe haber ordenado desbloquear las puertas para evacuación
        assertFalse(door.isLocked(), "La puerta debió desbloquearse automáticamente para evacuar");
        assertInstanceOf(AlarmTriggeredState.class, hub.getSecurityContext().getState());
    }
}

