package co.edu.uniquindio.smarthome.patterns.behavioral.command;

import co.edu.uniquindio.smarthome.patterns.structural.composite.SmartComponent;

public class TurnOnCommand implements ICommand {
    private final SmartComponent target;
    private boolean previousState;

    public TurnOnCommand(SmartComponent target) {
        this.target = target;
    }

    @Override
    public void execute() {
        this.previousState = target.isOn();
        target.turnOn();
    }

    @Override
    public void undo() {
        if (!previousState) {
            target.turnOff();
        } else {
            target.turnOn();
        }
    }

    @Override
    public String getDescription() {
        return "Encender: " + target.getName() + " (" + target.getDisplayLocation() + ")";
    }
}
