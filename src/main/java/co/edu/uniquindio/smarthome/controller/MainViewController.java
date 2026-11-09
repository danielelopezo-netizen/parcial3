package co.edu.uniquindio.smarthome.controller;

import co.edu.uniquindio.smarthome.model.DeviceType;
import co.edu.uniquindio.smarthome.model.SmartDevice;
import co.edu.uniquindio.smarthome.model.devices.*;
import co.edu.uniquindio.smarthome.patterns.behavioral.command.*;
import co.edu.uniquindio.smarthome.patterns.behavioral.observer.IObserver;
import co.edu.uniquindio.smarthome.patterns.behavioral.observer.SmartHomeEvent;
import co.edu.uniquindio.smarthome.patterns.behavioral.state.ISecurityState;
import co.edu.uniquindio.smarthome.patterns.behavioral.strategy.*;
import co.edu.uniquindio.smarthome.patterns.creational.factory.DeviceFactory;
import co.edu.uniquindio.smarthome.patterns.creational.singleton.SmartHomeCentralHub;
import co.edu.uniquindio.smarthome.patterns.structural.adapter.LegacyDeviceAdapter;
import co.edu.uniquindio.smarthome.patterns.structural.composite.RoomComposite;
import co.edu.uniquindio.smarthome.patterns.structural.composite.SmartComponent;
import co.edu.uniquindio.smarthome.service.DataSeedService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.UUID;

public class MainViewController implements IObserver {

    private final SmartHomeCentralHub hub = SmartHomeCentralHub.getInstance();

    // Header controls
    @FXML private Label lblSecurityBadge;
    @FXML private Label lblEnergyStrategyBadge;
    @FXML private Label lblActiveDevices;
    @FXML private Label lblPowerConsumption;
    @FXML private Button btnUndo;
    @FXML private Button btnRedo;

    // Dispositivos Tab
    @FXML private ComboBox<String> cmbRoomFilter;
    @FXML private FlowPane devicesFlowPane;
    @FXML private Button btnRoomTurnOn;
    @FXML private Button btnRoomTurnOff;

    // Formulario Crear Dispositivo (Factory Method)
    @FXML private ComboBox<DeviceType> cmbNewDeviceType;
    @FXML private TextField txtNewDeviceName;
    @FXML private ComboBox<String> cmbNewDeviceRoom;
    @FXML private Button btnCreateDevice;

    // Escenas & Comandos Tab
    @FXML private VBox scenesContainer;
    @FXML private ListView<String> lstCommandHistory;

    // Gestión Energética Tab
    @FXML private RadioButton rbEco;
    @FXML private RadioButton rbComfort;
    @FXML private RadioButton rbNight;
    @FXML private ToggleGroup energyGroup;
    @FXML private Label lblStrategyDesc;
    @FXML private Label lblSavingsPercent;
    @FXML private VBox roomConsumptionVBox;

    // Seguridad Tab
    @FXML private Label lblSecStateName;
    @FXML private Label lblSecStateDesc;
    @FXML private PasswordField txtPin;
    @FXML private Button btnArmHome;
    @FXML private Button btnArmAway;
    @FXML private Button btnDisarm;
    @FXML private Button btnSimulateMotion;
    @FXML private Button btnSimulateSmoke;

    // Monitor de Eventos Tab
    @FXML private ListView<String> lstEvents;

    @FXML
    public void initialize() {
        // Inicializar datos semilla
        DataSeedService.initializeSeedData(hub);

        // Registrar este controlador como observador para actualizar la UI reactivamente
        hub.registerObserver(this);

        // Configurar opciones de combo boxes
        setupFiltersAndForms();

        // Configurar RadioButtons de Estrategias Energéticas
        setupEnergyGroup();

        // Configurar Escenas
        renderScenes();

        // Enlazar listas de historial y eventos
        lstCommandHistory.setItems(hub.getCommandInvoker().getCommandHistoryDescriptions());

        // Actualizar vista completa
        refreshAllUI();
    }

    private void setupFiltersAndForms() {
        cmbRoomFilter.getItems().add("Todas las Habitaciones");
        for (RoomComposite room : hub.getRooms()) {
            cmbRoomFilter.getItems().add(room.getName());
            cmbNewDeviceRoom.getItems().add(room.getName());
        }
        cmbRoomFilter.getSelectionModel().selectFirst();
        if (!cmbNewDeviceRoom.getItems().isEmpty()) {
            cmbNewDeviceRoom.getSelectionModel().selectFirst();
        }

        cmbRoomFilter.setOnAction(e -> renderDeviceCards());

        // Tipos de dispositivos para la fábrica
        cmbNewDeviceType.setItems(FXCollections.observableArrayList(
                DeviceType.LIGHT,
                DeviceType.THERMOSTAT,
                DeviceType.LOCK,
                DeviceType.SECURITY_CAMERA,
                DeviceType.MOTION_SENSOR,
                DeviceType.SMOKE_SENSOR
        ));
        cmbNewDeviceType.getSelectionModel().selectFirst();
    }

    private void setupEnergyGroup() {
        energyGroup = new ToggleGroup();
        rbEco.setToggleGroup(energyGroup);
        rbComfort.setToggleGroup(energyGroup);
        rbNight.setToggleGroup(energyGroup);

        rbEco.setSelected(true);

        rbEco.setOnAction(e -> applySelectedStrategy(new EcoSaverStrategy()));
        rbComfort.setOnAction(e -> applySelectedStrategy(new ComfortPriorityStrategy()));
        rbNight.setOnAction(e -> applySelectedStrategy(new NightSecurityStrategy()));
    }

    private void applySelectedStrategy(IEnergyStrategy strategy) {
        hub.setEnergyStrategy(strategy);
        refreshAllUI();
    }

    @Override
    public void onEvent(SmartHomeEvent event) {
        Platform.runLater(() -> {
            lstEvents.getItems().add(0, event.toString());
            if (lstEvents.getItems().size() > 100) {
                lstEvents.getItems().remove(lstEvents.getItems().size() - 1);
            }
            refreshAllUI();
        });
    }

    public void refreshAllUI() {
        // 1. Header
        lblActiveDevices.setText(hub.getActiveDeviceCount() + " / " + hub.getTotalDeviceCount() + " Activos");
        lblPowerConsumption.setText(String.format("%.1f W", hub.getTotalPowerConsumptionWatts()));

        ISecurityState secState = hub.getSecurityContext().getState();
        lblSecurityBadge.setText(secState.getStateName());
        lblSecurityBadge.setStyle("-fx-background-color: " + secState.getBadgeColorHex() + "; -fx-text-fill: white; -fx-padding: 4 10; -fx-background-radius: 4; -fx-font-weight: bold;");

        IEnergyStrategy strat = hub.getCurrentEnergyStrategy();
        lblEnergyStrategyBadge.setText(strat.getName());

        btnUndo.setDisable(!hub.getCommandInvoker().canUndo());
        btnRedo.setDisable(!hub.getCommandInvoker().canRedo());

        // 2. Dispositivos
        renderDeviceCards();

        // 3. Tab Energía
        lblStrategyDesc.setText(strat.getDescription());
        lblSavingsPercent.setText(String.format("%.0f%% de Ahorro Estimado", strat.getEstimatedSavingsPercentage()));
        renderRoomConsumption();

        // 4. Tab Seguridad
        lblSecStateName.setText(secState.getStateName());
        lblSecStateName.setStyle("-fx-text-fill: " + secState.getBadgeColorHex() + "; -fx-font-size: 16px; -fx-font-weight: bold;");
        lblSecStateDesc.setText(secState.getDescription());
    }

    private void renderDeviceCards() {
        devicesFlowPane.getChildren().clear();
        String selectedFilter = cmbRoomFilter.getValue();

        for (RoomComposite room : hub.getRooms()) {
            if (selectedFilter != null && !selectedFilter.equals("Todas las Habitaciones") && !room.getName().equals(selectedFilter)) {
                continue;
            }

            for (SmartComponent component : room.getChildren()) {
                VBox card = createDeviceCard(component);
                devicesFlowPane.getChildren().add(card);
            }
        }
    }

    private VBox createDeviceCard(SmartComponent component) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(10));
        card.setPrefWidth(255);
        card.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 6; -fx-border-color: #cbd5e1; -fx-border-radius: 6; -fx-border-width: 1; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.04), 4, 0, 0, 1);");

        // Header de la tarjeta: Icono y Nombre
        HBox cardHeader = new HBox(8);
        cardHeader.setAlignment(Pos.CENTER_LEFT);

        Label lblIcon = new Label(getComponentIcon(component));
        lblIcon.setStyle("-fx-font-size: 18px;");

        VBox titleBox = new VBox(1);
        Label lblName = new Label(component.getName());
        lblName.setStyle("-fx-text-fill: #1e293b; -fx-font-weight: bold; -fx-font-size: 13px;");
        Label lblRoom = new Label(component.getDisplayLocation());
        lblRoom.setStyle("-fx-text-fill: #64748b; -fx-font-size: 11px;");
        titleBox.getChildren().addAll(lblName, lblRoom);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Toggle Switch Encendido / Apagado
        ToggleButton toggle = new ToggleButton(component.isOn() ? "ON" : "OFF");
        toggle.setSelected(component.isOn());
        updateToggleStyle(toggle, component.isOn());

        toggle.setOnAction(e -> {
            if (toggle.isSelected()) {
                hub.executeCommand(new TurnOnCommand(component));
            } else {
                hub.executeCommand(new TurnOffCommand(component));
            }
            refreshAllUI();
        });

        cardHeader.getChildren().addAll(lblIcon, titleBox, spacer, toggle);

        // Estado específico / detalles
        Label lblStatus = new Label();
        lblStatus.setWrapText(true);
        lblStatus.setStyle("-fx-text-fill: #475569; -fx-font-size: 11px;");

        if (component instanceof SmartDevice dev) {
            lblStatus.setText(dev.getSpecificStatusDescription());
        }

        // Métricas de potencia
        Label lblWatts = new Label(String.format("Consumo: %.1f W", component.getPowerConsumptionWatts()));
        lblWatts.setStyle("-fx-text-fill: #d97706; -fx-font-weight: bold; -fx-font-size: 11px;");

        card.getChildren().addAll(cardHeader, new Separator(), lblStatus, lblWatts);

        // Controles interactivos específicos según el tipo
        if (component instanceof SmartLight light) {
            HBox brightnessBox = new HBox(6);
            brightnessBox.setAlignment(Pos.CENTER_LEFT);
            Label bLabel = new Label("Brillo:");
            bLabel.setStyle("-fx-text-fill: #64748b; -fx-font-size: 11px;");
            Slider slider = new Slider(0, 100, light.getBrightnessPercentage());
            slider.valueProperty().addListener((obs, oldVal, newVal) -> {
                light.setBrightnessPercentage(newVal.intValue());
                refreshAllUI();
            });
            brightnessBox.getChildren().addAll(bLabel, slider);
            card.getChildren().add(brightnessBox);

        } else if (component instanceof SmartThermostat thermo) {
            HBox tempBox = new HBox(6);
            tempBox.setAlignment(Pos.CENTER_LEFT);
            Button btnMinus = new Button("-");
            btnMinus.setStyle("-fx-background-color: #e2e8f0; -fx-font-weight: bold; -fx-padding: 2 8; -fx-background-radius: 3;");
            Button btnPlus = new Button("+");
            btnPlus.setStyle("-fx-background-color: #e2e8f0; -fx-font-weight: bold; -fx-padding: 2 8; -fx-background-radius: 3;");
            Label lblTarget = new Label(String.format("%.1f°C", thermo.getTargetTemperature()));
            lblTarget.setStyle("-fx-text-fill: #2563eb; -fx-font-weight: bold;");

            btnMinus.setOnAction(e -> {
                hub.executeCommand(new SetTemperatureCommand(thermo, thermo.getTargetTemperature() - 0.5));
                refreshAllUI();
            });
            btnPlus.setOnAction(e -> {
                hub.executeCommand(new SetTemperatureCommand(thermo, thermo.getTargetTemperature() + 0.5));
                refreshAllUI();
            });

            tempBox.getChildren().addAll(new Label("Set:"), btnMinus, lblTarget, btnPlus);
            card.getChildren().add(tempBox);

        } else if (component instanceof SmartLock lock) {
            Button btnToggleLock = new Button(lock.isLocked() ? "Desbloquear" : "Bloquear");
            btnToggleLock.setMaxWidth(Double.MAX_VALUE);
            btnToggleLock.setStyle("-fx-background-color: #f1f5f9; -fx-text-fill: #1e293b; -fx-border-color: #cbd5e1; -fx-border-radius: 3; -fx-background-radius: 3; -fx-font-weight: bold;");
            btnToggleLock.setOnAction(e -> {
                if (lock.isLocked()) {
                    hub.executeCommand(new UnlockDoorCommand(lock));
                } else {
                    hub.executeCommand(new LockDoorCommand(lock));
                }
                refreshAllUI();
            });
            card.getChildren().add(btnToggleLock);

        } else if (component instanceof LegacyDeviceAdapter legacy) {
            HBox levelBox = new HBox(6);
            levelBox.setAlignment(Pos.CENTER_LEFT);
            Label lLabel = new Label("Nivel A/C:");
            lLabel.setStyle("-fx-text-fill: #64748b; -fx-font-size: 11px;");
            Slider slider = new Slider(1, 5, legacy.getCoolingIntensity());
            slider.setSnapToTicks(true);
            slider.setMajorTickUnit(1);
            slider.valueProperty().addListener((obs, oldVal, newVal) -> {
                legacy.setCoolingIntensity(newVal.intValue());
                refreshAllUI();
            });
            levelBox.getChildren().addAll(lLabel, slider);
            card.getChildren().add(levelBox);
        }

        return card;
    }

    private void updateToggleStyle(ToggleButton toggle, boolean on) {
        if (on) {
            toggle.setText("ON");
            toggle.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4; -fx-padding: 3 8;");
        } else {
            toggle.setText("OFF");
            toggle.setStyle("-fx-background-color: #e2e8f0; -fx-text-fill: #475569; -fx-font-weight: bold; -fx-background-radius: 4; -fx-padding: 3 8;");
        }
    }

    private String getComponentIcon(SmartComponent component) {
        if (component instanceof SmartDevice dev) {
            return dev.getType().getIcon();
        }
        return "🏠";
    }

    private void renderScenes() {
        scenesContainer.getChildren().clear();
        for (MacroSceneCommand scene : hub.getAvailableScenes()) {
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(10, 14, 10, 14));
            row.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 6; -fx-border-color: #cbd5e1; -fx-border-radius: 6; -fx-border-width: 1; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.03), 3, 0, 0, 1);");

            Label lblIcon = new Label(scene.getIcon());
            lblIcon.setStyle("-fx-font-size: 20px;");

            VBox info = new VBox(2);
            Label lblTitle = new Label(scene.getSceneName());
            lblTitle.setStyle("-fx-text-fill: #1e293b; -fx-font-weight: bold; -fx-font-size: 13px;");
            Label lblDesc = new Label(scene.getDescription());
            lblDesc.setStyle("-fx-text-fill: #64748b; -fx-font-size: 11px;");
            info.getChildren().addAll(lblTitle, lblDesc);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Button btnRun = new Button("Ejecutar");
            btnRun.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 14; -fx-background-radius: 4;");
            btnRun.setOnAction(e -> {
                hub.executeCommand(scene);
                refreshAllUI();
            });

            row.getChildren().addAll(lblIcon, info, spacer, btnRun);
            scenesContainer.getChildren().add(row);
        }
    }

    private void renderRoomConsumption() {
        roomConsumptionVBox.getChildren().clear();
        for (RoomComposite room : hub.getRooms()) {
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(6, 12, 6, 12));
            row.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 4; -fx-border-color: #e2e8f0; -fx-border-radius: 4; -fx-border-width: 1;");

            Label lblName = new Label("Habitación: " + room.getName());
            lblName.setStyle("-fx-text-fill: #1e293b; -fx-font-weight: bold;");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Label lblWatts = new Label(String.format("%.1f Watts", room.getPowerConsumptionWatts()));
            lblWatts.setStyle("-fx-text-fill: #d97706; -fx-font-weight: bold;");

            row.getChildren().addAll(lblName, spacer, lblWatts);
            roomConsumptionVBox.getChildren().add(row);
        }
    }

    // --- Acciones de interfaz ---

    @FXML
    private void onUndoClicked() {
        hub.undoCommand();
        refreshAllUI();
    }

    @FXML
    private void onRedoClicked() {
        hub.redoCommand();
        refreshAllUI();
    }

    @FXML
    private void onTurnRoomOn() {
        String selected = cmbRoomFilter.getValue();
        if (selected == null || selected.equals("Todas las Habitaciones")) {
            for (RoomComposite room : hub.getRooms()) {
                hub.executeCommand(new TurnOnCommand(room));
            }
        } else {
            hub.getRooms().stream()
                    .filter(r -> r.getName().equals(selected))
                    .findFirst()
                    .ifPresent(r -> hub.executeCommand(new TurnOnCommand(r)));
        }
        refreshAllUI();
    }

    @FXML
    private void onTurnRoomOff() {
        String selected = cmbRoomFilter.getValue();
        if (selected == null || selected.equals("Todas las Habitaciones")) {
            for (RoomComposite room : hub.getRooms()) {
                hub.executeCommand(new TurnOffCommand(room));
            }
        } else {
            hub.getRooms().stream()
                    .filter(r -> r.getName().equals(selected))
                    .findFirst()
                    .ifPresent(r -> hub.executeCommand(new TurnOffCommand(r)));
        }
        refreshAllUI();
    }

    @FXML
    private void onCreateDevice() {
        DeviceType type = cmbNewDeviceType.getValue();
        String name = txtNewDeviceName.getText();
        String roomName = cmbNewDeviceRoom.getValue();

        if (name == null || name.trim().isEmpty() || roomName == null || type == null) {
            showAlert("Validación", "Por favor ingrese un nombre y seleccione habitación y tipo.");
            return;
        }

        // Crear dispositivo con Factory Method
        String newId = "DEV-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        SmartDevice newDevice = DeviceFactory.createDevice(type, newId, name.trim(), roomName);

        // Agregar al Composite de la habitación
        for (RoomComposite r : hub.getRooms()) {
            if (r.getName().equals(roomName)) {
                r.addComponent(newDevice);
                break;
            }
        }

        txtNewDeviceName.clear();
        refreshAllUI();

        showAlert("Dispositivo Creado", "Dispositivo creado exitosamente mediante Factory Method: " + newDevice.getName());
    }

    @FXML
    private void onArmHome() {
        hub.getSecurityContext().armHome();
        refreshAllUI();
    }

    @FXML
    private void onArmAway() {
        hub.getSecurityContext().armAway();
        hub.getMediator().notify(this, "SECURITY_ARMED_AWAY", null);
        refreshAllUI();
    }

    @FXML
    private void onDisarm() {
        String pin = txtPin.getText();
        if (pin == null || pin.trim().isEmpty()) {
            showAlert("Seguridad", "Ingrese el PIN para desarmar el sistema (Por defecto: 1234).");
            return;
        }
        hub.getSecurityContext().disarm(pin.trim());
        txtPin.clear();
        refreshAllUI();
    }

    @FXML
    private void onSimulateMotion() {
        // Disparar a través del mediador
        hub.getMediator().notify(this, "MOTION_DETECTED", "Patio & Entrada");
        refreshAllUI();
    }

    @FXML
    private void onSimulateSmoke() {
        // Disparar a través del mediador: esto disparará el protocolo de evacuación automática
        hub.getMediator().notify(this, "SMOKE_DETECTED", "Cocina Integral");
        refreshAllUI();
    }

    @FXML
    private void onClearEvents() {
        lstEvents.getItems().clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
