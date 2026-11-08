package co.edu.uniquindio.smarthome.patterns.behavioral.command;

import co.edu.uniquindio.smarthome.model.devices.SmartLock;

public class LockDoorCommand implements ICommand {
    private final SmartLock lock;
    private boolean wasLocked;

    public LockDoorCommand(SmartLock lock) {
        this.lock = lock;
    }

    @Override
    public void execute() {
        this.wasLocked = lock.isLocked();
        lock.lock();
    }

    @Override
    public void undo() {
        if (!wasLocked) {
            lock.unlock();
        }
    }

    @Override
    public String getDescription() {
        return "Echar cerrojo a: " + lock.getName();
    }
}
