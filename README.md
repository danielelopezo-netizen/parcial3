# 🏡 SmartHomeFX - Sistema de Gestión y Automatización para Hogares Inteligentes

[![Java 17](https://img.shields.io/badge/Java-17%20LTS-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![JavaFX](https://img.shields.io/badge/JavaFX-17-blue.svg)](https://openjfx.io/)
[![Patrones GoF](https://img.shields.io/badge/Patrones-GoF%20(8+)-green.svg)]()
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)]()

Proyecto correspondiente al **Parcial 3 - Patrones de Diseño de Software** de la **Universidad del Quindío**.  
Aplicación de escritorio interactiva desarrollada con **Java 17/21, JavaFX y FXML** para la administración, supervisión reactiva y automatización coordinada de dispositivos y zonas en un hogar inteligente (*Smart Home Management System*).

---

## 📋 Descripción General del Proyecto

**SmartHomeFX** es una plataforma centralizada que permite a los residentes de una vivienda inteligente gestionar estancias, supervisar el consumo eléctrico en tiempo real, programar rutinas automatizadas (escenas) y salvaguardar la seguridad del hogar mediante protocolos reactivos ante emergencias.

### ✨ Funcionalidades Principales

1. **Gestión Jerárquica de Zonas y Dispositivos:** Visualización interactiva y control por habitaciones (Sala Principal, Cocina Integral, Habitación Master, Patio Exterior) o individualmente (luces dimerizables, termostatos inteligentes, cerraduras digitales, cámaras de vigilancia y sensores).
2. **Control en Lote y Agregación Energética:** Capacidad para encender, apagar o medir el consumo eléctrico de una habitación o de toda la vivienda de manera uniforme con una sola instrucción.
3. **Automatización mediante Macro-Escenas:** Ejecución atómica de escenas predefinidas (*Modo Cine*, *Salir de Casa*, *Buenas Noches*, *Llegada al Hogar*) con historial de comandos y capacidad completa de **Deshacer (Undo)** y **Rehacer (Redo)**.
4. **Políticas de Optimización Energética:** Conmutación dinámica de estrategias (*Eco-Saver*, *Máximo Confort*, *Noche & Seguridad*) que recalculan y adaptan el comportamiento de los actuadores reduciendo el consumo en vatios.
5. **Sistema de Seguridad Multi-Estado:** Gestión reactiva entre 4 estados operacionales (*Desarmado*, *Armado en Casa*, *Armado Fuera*, *¡Alarma Activada!*) con validación de clave PIN para desactivación de sirena.
6. **Mediación de Emergencias e Incidentes:** Protocolos automáticos coordinados (ej. ante detección de humo, el mediador desbloquea de inmediato todas las cerraduras para facilitar la evacuación, enciende iluminación de emergencia en rojo y dispara la alerta sonora).
7. **Integración de Dispositivos Heredados (Legacy):** Conexión transparente de electrodomésticos antiguos (A/C clásico con protocolo analógico incompatible) a la plataforma moderna.
8. **Consola de Eventos en Tiempo Real:** Monitor reactivo que registra y muestra con marcas de tiempo y niveles de severidad cada suceso del hogar inteligente.

---

## 🛠️ Tecnologías y Requisitos

- **Lenguaje:** Java 17 LTS o Java 21 LTS
- **Interfaz Gráfica:** JavaFX 17 / FXML con hojas de estilo CSS modernas
- **Gestor de Dependencias y Construcción:** Apache Maven 3.8+
- **Pruebas Unitarias:** JUnit Jupiter 5.10.2

---

## 🚀 Instrucciones para Configurar y Ejecutar

### Opción 1: Compilación y Ejecución mediante Maven

1. Abra una terminal en la raíz del proyecto.
2. Compile y ejecute la aplicación con el comando:

```bash
mvn clean compile exec:java
```

O utilizando el plugin oficial de JavaFX:

```bash
mvn javafx:run
```

### Opción 2: Ejecutar la Suite de Pruebas Unitarias

Para verificar el correcto funcionamiento de todos los patrones de diseño y lógica de negocio:

```bash
mvn clean test
```

---

## 📁 Estructura General del Proyecto

```
parcial3/
├── pom.xml                                      # Configuración Maven y dependencias
├── README.md                                    # Documentación completa del proyecto
└── src/
    ├── main/
    │   ├── java/co/edu/uniquindio/smarthome/
    │   │   ├── AppLauncher.java                 # Punto de entrada para el empaquetado JAR
    │   │   ├── MainApp.java                     # Clase Application principal de JavaFX
    │   │   ├── controller/
    │   │   │   └── MainViewController.java     # Controlador FXML y observador de UI
    │   │   ├── model/
    │   │   │   ├── DeviceStatus.java            # Enums de estado de dispositivos
    │   │   │   ├── DeviceType.java              # Enums de tipos de dispositivos y metadatos
    │   │   │   ├── SmartDevice.java             # Hoja base abstracta (Leaf en Composite)
    │   │   │   └── devices/
    │   │   │       ├── SmartLight.java          # Iluminación inteligente dimerizable
    │   │   │       ├── SmartThermostat.java     # Termostato y climatización HVAC
    │   │   │       ├── SmartLock.java           # Cerradura digital con PIN y cerrojo
    │   │   │       ├── SmartCamera.java         # Cámara perimetral de seguridad
    │   │   │       └── SmartSensor.java         # Sensores de movimiento y humo
    │   │   ├── patterns/
    │   │   │   ├── behavioral/                  # Patrones de comportamiento (5 OBLIGATORIOS)
    │   │   │   │   ├── command/                 # Command: Comandos, macros y soporte Undo/Redo
    │   │   │   │   ├── mediator/                # Mediator: Coordinación centralizada de eventos
    │   │   │   │   ├── observer/                # Observer: Notificaciones reactivas en tiempo real
    │   │   │   │   ├── state/                   # State: Máquina de estados del sistema de seguridad
    │   │   │   │   └── strategy/                # Strategy: Algoritmos de optimización de energía
    │   │   │   ├── creational/                  # Patrones creacionales
    │   │   │   │   ├── factory/                 # Factory Method: Creación polimórfica de dispositivos
    │   │   │   │   └── singleton/               # Singleton: Centro de control SmartHomeCentralHub
    │   │   │   └── structural/                  # Patrones estructurales
    │   │   │       ├── adapter/                 # Adapter: Integración de A/C clásico legacy
    │   │   │       └── composite/               # Composite: Árbol de zonas/habitaciones y dispositivos
    │   │   └── service/
    │   │       └── DataSeedService.java         # Carga inicial de datos semilla
    │   └── resources/co/edu/uniquindio/smarthome/view/
    │       ├── main-view.fxml                   # Definición declarativa de la interfaz FXML
    │       └── styles.css                       # Estilos CSS Dark Theme modernos
    └── test/java/co/edu/uniquindio/smarthome/
        └── PatternsTest.java                    # Suite integral de pruebas JUnit 5 de los patrones
```

---

## 🧩 Catálogo y Ubicación de Patrones de Diseño

### 1. Patrones de Comportamiento (Obligatorios - 40 Puntos)

| Patrón | Paquete / Archivos Clave | Problema que Resuelve en el Dominio | Cómo se Utiliza en el Sistema |
| :--- | :--- | :--- | :--- |
| **Observer** | `patterns.behavioral.observer`<br>- `ISubject.java`<br>- `IObserver.java`<br>- `SmartHomeEvent.java`<br>- `EventLogObserver.java`<br>- `SecurityAlertObserver.java` | Desacoplar la generación de sucesos (sensores, cambios de estado, ejecución de comandos) de su visualización y tratamiento. | `SmartHomeCentralHub` actúa como Sujeto observable. Cuando un sensor detecta humo, una luz cambia de estado o se ejecuta una escena, se notifica a los observadores (`EventLogObserver`, controlador gráfico de JavaFX y monitores de seguridad) sin acoplamiento directo. |
| **Strategy** | `patterns.behavioral.strategy`<br>- `IEnergyStrategy.java`<br>- `EcoSaverStrategy.java`<br>- `ComfortPriorityStrategy.java`<br>- `NightSecurityStrategy.java` | Permitir el intercambio dinámico de algoritmos de optimización de consumo eléctrico sin alterar las clases de los dispositivos. | El usuario puede conmutar en caliente entre modo ecológico (*Eco-Saver* que reduce brillo al 50% y eleva termostatos a 24.5°C), modo confort (21.5°C y 90% brillo) o modo nocturno/fuera (apaga luces y climatización no crítica). |
| **Command** | `patterns.behavioral.command`<br>- `ICommand.java`<br>- `TurnOnCommand.java`<br>- `TurnOffCommand.java`<br>- `SetTemperatureCommand.java`<br>- `LockDoorCommand.java`<br>- `MacroSceneCommand.java`<br>- `CommandInvoker.java` | Encapsular peticiones como objetos independientes, permitiendo parametrizar acciones, componer rutinas y habilitar Deshacer/Rehacer (*Undo/Redo*). | Cada botón de la interfaz genera un comando que guarda el estado previo del componente. `CommandInvoker` gestiona pilas de deshacer y rehacer. `MacroSceneCommand` agrupa órdenes para ejecutar escenas complejas (*Modo Cine*, *Salir de Casa*) de manera atómica y reversible. |
| **State** | `patterns.behavioral.state`<br>- `ISecurityState.java`<br>- `DisarmedState.java`<br>- `ArmedHomeState.java`<br>- `ArmedAwayState.java`<br>- `AlarmTriggeredState.java`<br>- `SecurityContext.java` | Alterar el comportamiento del sistema de seguridad según su estado actual, evitando condicionales anidados complejos (`if/switch`). | El sistema reacciona de forma completamente distinta ante el mismo estímulo (detección de movimiento): en `DisarmedState` solo genera un registro informativo; en `ArmedHomeState` tolera movimiento interior pero vigila puertas; en `ArmedAwayState` dispara la sirena; y en `AlarmTriggeredState` bloquea operaciones hasta ingresar el PIN de desbloqueo. |
| **Mediator** | `patterns.behavioral.mediator`<br>- `ISmartHomeMediator.java`<br>- `SmartHomeHubMediator.java` | Evitar el acoplamiento directo *muchos-a-muchos* entre sensores, cerraduras, luces y el sistema de seguridad ante emergencias. | Cuando el sensor de humo detecta peligro, notifica al `SmartHomeHubMediator`. Este orquesta simultáneamente: poner la seguridad en alarma, desbloquear de inmediato todas las puertas para evacuar, encender luces en rojo y registrar la alerta sin que el sensor conozca a las cerraduras ni a las bombillas. |

---

### 2. Patrones Adicionales Seleccionados (Creacionales y Estructurales - 15 Puntos)

La selección cumple con tener al menos un patrón creacional, al menos un patrón estructural y una suma de complejidad de **7 puntos** (superando el umbral mínimo de 5):

| Categoría | Patrón | Peso | Paquete / Archivos Clave | Justificación y Problema que Resuelve en el Dominio |
| :--- | :--- | :---: | :--- | :--- |
| **Creacional** | **Factory Method** | **2** | `patterns.creational.factory`<br>- `DeviceFactory.java` | **Justificación:** Centraliza y desacopla la instanciación de los múltiples tipos de dispositivos inteligentes (`SmartLight`, `SmartThermostat`, `SmartLock`, `SmartCamera`, `SmartSensor`). La interfaz y el controlador solicitan un dispositivo especificando su `DeviceType` y ubicación, y la fábrica se encarga de instanciar la clase concreta adecuada con sus valores de consumo y parámetros base. |
| **Estructural** | **Composite** | **3** | `patterns.structural.composite`<br>- `SmartComponent.java`<br>- `RoomComposite.java`<br>- `SmartDevice.java` | **Justificación:** Representa la estructura natural y jerárquica de una vivienda (habitaciones, zonas y viviendas completas que contienen dispositivos o sub-estancias). Trata de forma idéntica y polimórfica a una sola bombilla o a una habitación completa: invocar `turnOff()` o `getPowerConsumptionWatts()` sobre `RoomComposite` delega recursivamente en todos sus hijos, permitiendo control masivo y agregación energética transparente. |
| **Estructural** | **Adapter** | **2** | `patterns.structural.adapter`<br>- `LegacyAirConditioner.java`<br>- `LegacyDeviceAdapter.java` | **Justificación:** Permite integrar dispositivos antiguos o de terceros que no poseen capacidades IoT nativas y tienen interfaces incompatibles (ej. métodos analógicos como `sendPowerPulse(int)` y `measureAnalogWattDraw()`). `LegacyDeviceAdapter` envuelve al aire acondicionado clásico traduciéndolo a la interfaz estándar `SmartDevice` sin alterar el código original del fabricante. |
| **Creacional** | **Singleton** | **1** | `patterns.creational.singleton`<br>- `SmartHomeCentralHub.java` | **Justificación:** Garantiza un único punto central de coordinación global para el hogar inteligente, manteniendo consistencia en el estado de las habitaciones, el contexto de seguridad, el bus de eventos y el historial de comandos en memoria. |

**Total de Complejidad Adicional:** 2 + 3 + 2 = **7 puntos** *(Válido y superior al mínimo de 5)*.

---

## 👥 Datos de Prueba y Credenciales Rápidas

Para probar el desarme del sistema de seguridad o silenciar una alarma disparada:
- **PIN de Seguridad por defecto:** `1234`
- **Prueba de Mediador y Evacuación:** Presione el botón *"🔥 Simular Alerta de Humo"* en la pestaña de Seguridad para observar cómo el mediador activa la alarma, desbloquea las cerraduras y enciende luces de emergencia automáticamente.
- **Prueba de Undo / Redo:** Presione *"Ejecutar Escena Modo Cine"* o apague una habitación y luego presione *"↩ Deshacer"* en la barra superior para restaurar los estados exactos anteriores.

---

## 👤 Autor

- **Desarrollador:** [@danielelopezo-netizen](https://github.com/danielelopezo-netizen)


