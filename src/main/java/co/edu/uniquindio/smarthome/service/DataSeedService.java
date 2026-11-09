package co.edu.uniquindio.smarthome.service;

import co.edu.uniquindio.smarthome.model.DeviceType;
import co.edu.uniquindio.smarthome.model.SmartDevice;
import co.edu.uniquindio.smarthome.model.devices.*;
import co.edu.uniquindio.smarthome.patterns.behavioral.command.*;
import co.edu.uniquindio.smarthome.patterns.creational.factory.DeviceFactory;
import co.edu.uniquindio.smarthome.patterns.creational.singleton.SmartHomeCentralHub;
import co.edu.uniquindio.smarthome.patterns.structural.adapter.LegacyAirConditioner;
import co.edu.uniquindio.smarthome.patterns.structural.adapter.LegacyDeviceAdapter;
import co.edu.uniquindio.smarthome.patterns.structural.composite.RoomComposite;

/**
 * Servicio encargado de inicializar los datos semilla del hogar inteligente,
 * creando las habitaciones (Composite), instanciando dispositivos mediante la fábrica
 * (Factory Method), adaptando electrodomésticos clásicos (Adapter) y configurando
 * las macro escenas predeterminadas (Command).
 */
public class DataSeedService {

    public static void initializeSeedData(SmartHomeCentralHub hub) {
        // 1. Crear Habitaciones Compuestas (Patrón Composite)
        RoomComposite livingRoom = new RoomComposite("ROOM-01", "Sala Principal", "Planta Baja - Zona Social");
        RoomComposite kitchen = new RoomComposite("ROOM-02", "Cocina Integral", "Planta Baja - Zona Servicio");
        RoomComposite masterBedroom = new RoomComposite("ROOM-03", "Habitación Master", "Planta Alta - Zona Privada");
        RoomComposite patio = new RoomComposite("ROOM-04", "Patio & Entrada", "Exterior - Acceso Perimetral");

        // 2. Crear Dispositivos con la Fábrica (Patrón Factory Method)
        SmartDevice lightLiving = DeviceFactory.createDevice(DeviceType.LIGHT, "DEV-01", "Lámpara Principal Techo", "Sala Principal");
        SmartDevice thermoLiving = DeviceFactory.createDevice(DeviceType.THERMOSTAT, "DEV-02", "Termostato Central Sala", "Sala Principal");
        SmartDevice motionLiving = DeviceFactory.createDevice(DeviceType.MOTION_SENSOR, "DEV-03", "Sensor Movimiento Sala", "Sala Principal");

        SmartDevice lightKitchen = DeviceFactory.createDevice(DeviceType.LIGHT, "DEV-04", "Luz Encimera Cocina", "Cocina Integral");
        SmartDevice smokeKitchen = DeviceFactory.createDevice(DeviceType.SMOKE_SENSOR, "DEV-05", "Sensor Humo Cocina", "Cocina Integral");

        // Dispositivo Adaptado (Patrón Adapter) en la cocina/despensa
        LegacyAirConditioner vintageAc = new LegacyAirConditioner("AC-PANASONIC-1998", "Cocina Integral");
        LegacyDeviceAdapter adapterAc = new LegacyDeviceAdapter("DEV-06", "A/C Frigorífico Adaptado", vintageAc);

        SmartDevice lightBedroom = DeviceFactory.createDevice(DeviceType.LIGHT, "DEV-07", "Iluminación Dimmer Cuarto", "Habitación Master");
        SmartDevice thermoBedroom = DeviceFactory.createDevice(DeviceType.THERMOSTAT, "DEV-08", "Climatizador Dormitorio", "Habitación Master");
        SmartDevice lockBedroom = DeviceFactory.createDevice(DeviceType.LOCK, "DEV-09", "Cerradura Digital Puerta Master", "Habitación Master");

        SmartDevice lockFrontDoor = DeviceFactory.createDevice(DeviceType.LOCK, "DEV-10", "Cerradura Portón Principal", "Patio & Entrada");
        SmartDevice cameraFront = DeviceFactory.createDevice(DeviceType.SECURITY_CAMERA, "DEV-11", "Cámara Exterior Perimetral", "Patio & Entrada");
        SmartDevice motionPatio = DeviceFactory.createDevice(DeviceType.MOTION_SENSOR, "DEV-12", "Sensor Presencia Entrada", "Patio & Entrada");

        // Encender algunos dispositivos de inicio
        lightLiving.turnOn();
        thermoLiving.turnOn();
        lightKitchen.turnOn();
        lockFrontDoor.turnOn();
        cameraFront.turnOn();

        // 3. Agregar componentes a las habitaciones (Composite)
        livingRoom.addComponent(lightLiving);
        livingRoom.addComponent(thermoLiving);
        livingRoom.addComponent(motionLiving);

        kitchen.addComponent(lightKitchen);
        kitchen.addComponent(smokeKitchen);
        kitchen.addComponent(adapterAc);

        masterBedroom.addComponent(lightBedroom);
        masterBedroom.addComponent(thermoBedroom);
        masterBedroom.addComponent(lockBedroom);

        patio.addComponent(lockFrontDoor);
        patio.addComponent(cameraFront);
        patio.addComponent(motionPatio);

        // Registrar habitaciones en el Hub
        hub.addRoom(livingRoom);
        hub.addRoom(kitchen);
        hub.addRoom(masterBedroom);
        hub.addRoom(patio);

        // 4. Configurar Escenas Predeterminadas (MacroCommand)
        setupMacroScenes(hub, lightLiving, lightKitchen, lightBedroom, thermoLiving, (SmartLock) lockFrontDoor, (SmartLock) lockBedroom);
    }

    private static void setupMacroScenes(SmartHomeCentralHub hub,
                                         SmartDevice lightLiving,
                                         SmartDevice lightKitchen,
                                         SmartDevice lightBedroom,
                                         SmartDevice thermoLiving,
                                         SmartLock lockFront,
                                         SmartLock lockBedroom) {

        // Escena 1: Modo Cine (Movie Night)
        MacroSceneCommand movieNight = new MacroSceneCommand("Modo Cine", "🍿");
        movieNight.addCommand(new TurnOffCommand(lightKitchen));
        if (lightLiving instanceof SmartLight sLight) {
            movieNight.addCommand(new ICommand() {
                private int prevBrightness;
                @Override
                public void execute() {
                    prevBrightness = sLight.getBrightnessPercentage();
                    sLight.turnOn();
                    sLight.setBrightnessPercentage(25);
                    sLight.setColorHex("#3498DB"); // Azul tenue ambiente
                }
                @Override
                public void undo() {
                    sLight.setBrightnessPercentage(prevBrightness);
                }
                @Override
                public String getDescription() {
                    return "Atenuar luz sala a 25% azul noche";
                }
            });
        }
        if (thermoLiving instanceof SmartThermostat sThermo) {
            movieNight.addCommand(new SetTemperatureCommand(sThermo, 21.0));
        }
        movieNight.addCommand(new LockDoorCommand(lockFront));
        hub.addScene(movieNight);

        // Escena 2: Salir de Casa (Leaving Home)
        MacroSceneCommand leavingHome = new MacroSceneCommand("Salir de Casa", "🏃");
        for (RoomComposite room : hub.getRooms()) {
            leavingHome.addCommand(new TurnOffCommand(room));
        }
        leavingHome.addCommand(new LockDoorCommand(lockFront));
        leavingHome.addCommand(new LockDoorCommand(lockBedroom));
        hub.addScene(leavingHome);

        // Escena 3: Buenas Noches (Good Night)
        MacroSceneCommand goodNight = new MacroSceneCommand("Buenas Noches", "🛌");
        goodNight.addCommand(new TurnOffCommand(lightLiving));
        goodNight.addCommand(new TurnOffCommand(lightKitchen));
        goodNight.addCommand(new LockDoorCommand(lockFront));
        goodNight.addCommand(new LockDoorCommand(lockBedroom));
        hub.addScene(goodNight);

        // Escena 4: Bienvenida / Llegar a Casa (Welcome Home)
        MacroSceneCommand welcomeHome = new MacroSceneCommand("Llegada al Hogar", "🎉");
        welcomeHome.addCommand(new TurnOnCommand(lightLiving));
        welcomeHome.addCommand(new TurnOnCommand(lightKitchen));
        welcomeHome.addCommand(new UnlockDoorCommand(lockFront));
        hub.addScene(welcomeHome);
    }
}
