package co.edu.uniquindio.smarthome.patterns.creational.factory;

import co.edu.uniquindio.smarthome.model.DeviceType;
import co.edu.uniquindio.smarthome.model.SmartDevice;
import co.edu.uniquindio.smarthome.model.devices.*;
import co.edu.uniquindio.smarthome.patterns.structural.adapter.LegacyAirConditioner;
import co.edu.uniquindio.smarthome.patterns.structural.adapter.LegacyDeviceAdapter;

/**
 * Patrón Creacional: Factory Method
 * Centraliza la creación e instanciación desacoplada de dispositivos inteligentes
 * a partir de un tipo y parámetros de configuración.
 */
public class DeviceFactory {

    public static SmartDevice createDevice(DeviceType type, String id, String name, String roomLocation) {
        return switch (type) {
            case LIGHT -> new SmartLight(id, name, roomLocation, 12.0); // 12 Watts LED
            case THERMOSTAT -> new SmartThermostat(id, name, roomLocation, 120.0); // 120 Watts A/C o caldera
            case LOCK -> new SmartLock(id, name, roomLocation, 2.5);
            case SECURITY_CAMERA -> new SmartCamera(id, name, roomLocation, 8.0);
            case MOTION_SENSOR -> new SmartSensor(id, name, DeviceType.MOTION_SENSOR, roomLocation);
            case SMOKE_SENSOR -> new SmartSensor(id, name, DeviceType.SMOKE_SENSOR, roomLocation);
            case LEGACY_ADAPTER -> {
                LegacyAirConditioner legacyAc = new LegacyAirConditioner("AC-VINTAGE-99", roomLocation);
                yield new LegacyDeviceAdapter(id, name, legacyAc);
            }
        };
    }
}
