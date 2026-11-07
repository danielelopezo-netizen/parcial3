package co.edu.uniquindio.smarthome.patterns.behavioral.command;

/**
 * Patrón de Comportamiento: Command
 * Interfaz que define las operaciones ejecutables y reversibles (Undo / Redo).
 */
public interface ICommand {
    void execute();
    void undo();
    String getDescription();
}
