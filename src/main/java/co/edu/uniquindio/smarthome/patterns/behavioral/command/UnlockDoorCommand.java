package co.edu.uniquindio.smarthome.patterns.behavioral.command;

import co.edu.uniquindio.smarthome.model.devices.SmartLock;

public class UnlockDoorCommand implements ICommand {
    private final SmartLock lock;
    private boolean wasLocked;

    public UnlockDoorCommand(SmartLock lock) {
        this.lock = lock;
    }

    @Override
    public void execute() {
        this.wasLocked = lock.isLocked();
        lock.unlock();
    }

    @Override
    public void undo() {
        if (wasLocked) {
            lock.lock();
        }
    }

    @Override
    public String getDescription() {
        return "Desbloquear cerradura: " + lock.getName();
    }
}
