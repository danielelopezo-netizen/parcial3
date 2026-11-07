package co.edu.uniquindio.smarthome.patterns.behavioral.command;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;

/**
 * Invocador del patrón Command.
 * Mantiene la pila de deshacer (Undo) y rehacer (Redo), y notifica
 * las operaciones realizadas.
 */
public class CommandInvoker {
    private final Deque<ICommand> undoStack;
    private final Deque<ICommand> redoStack;
    private final ObservableList<String> commandHistoryDescriptions;
    private Consumer<ICommand> onCommandExecutedCallback;

    public CommandInvoker() {
        this.undoStack = new ArrayDeque<>();
        this.redoStack = new ArrayDeque<>();
        this.commandHistoryDescriptions = FXCollections.observableArrayList();
    }

    public void setOnCommandExecutedCallback(Consumer<ICommand> onCommandExecutedCallback) {
        this.onCommandExecutedCallback = onCommandExecutedCallback;
    }

    public synchronized void executeCommand(ICommand command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear(); // Limpiar el redo tras nueva acción

        String desc = "▶ " + command.getDescription();
        commandHistoryDescriptions.add(0, desc);
        if (commandHistoryDescriptions.size() > 50) {
            commandHistoryDescriptions.remove(commandHistoryDescriptions.size() - 1);
        }

        if (onCommandExecutedCallback != null) {
            onCommandExecutedCallback.accept(command);
        }
    }

    public synchronized boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public synchronized boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public synchronized ICommand undo() {
        if (!canUndo()) return null;
        ICommand cmd = undoStack.pop();
        cmd.undo();
        redoStack.push(cmd);

        commandHistoryDescriptions.add(0, "↩ [UNDO] " + cmd.getDescription());
        return cmd;
    }

    public synchronized ICommand redo() {
        if (!canRedo()) return null;
        ICommand cmd = redoStack.pop();
        cmd.execute();
        undoStack.push(cmd);

        commandHistoryDescriptions.add(0, "🔁 [REDO] " + cmd.getDescription());
        return cmd;
    }

    public ObservableList<String> getCommandHistoryDescriptions() {
        return commandHistoryDescriptions;
    }
}
