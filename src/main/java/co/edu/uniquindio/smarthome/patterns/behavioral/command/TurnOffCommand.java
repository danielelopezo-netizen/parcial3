package co.edu.uniquindio.smarthome.patterns.behavioral.command;

import co.edu.uniquindio.smarthome.patterns.structural.composite.SmartComponent;

public class TurnOffCommand implements ICommand {
    private final SmartComponent target;
    private boolean previousState;

    public TurnOffCommand(SmartComponent target) {
        this.target = target;
    }

    @Override
    public void execute() {
        this.previousState = target.isOn();
        target.turnOff();
    }

    @Override
    public void undo() {
        if (previousState) {
            target.turnOn();
        } else {
            target.turnOff();
        }
    }

    @Override
    public String getDescription() {
        return "Apagar: " + target.getName() + " (" + target.getDisplayLocation() + ")";
    }
}
