package co.edu.uniquindio.smarthome.patterns.behavioral.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Macro Command / Composite Command:
 * Agrupa múltiples comandos para ser ejecutados como una escena o rutina automatizada
 * (ej. "Modo Cine", "Salir de Casa", "Buenas Noches").
 * Al deshacerse (undo), invierte el orden de ejecución para preservar consistencia.
 */
public class MacroSceneCommand implements ICommand {
    private final String sceneName;
    private final String icon;
    private final List<ICommand> commands;

    public MacroSceneCommand(String sceneName, String icon) {
        this.sceneName = sceneName;
        this.icon = icon;
        this.commands = new ArrayList<>();
    }

    public void addCommand(ICommand command) {
        if (command != null) {
            commands.add(command);
        }
    }

    @Override
    public void execute() {
        for (ICommand cmd : commands) {
            cmd.execute();
        }
    }

    @Override
    public void undo() {
        // En reversa para deshacer correctamente
        for (int i = commands.size() - 1; i >= 0; i--) {
            commands.get(i).undo();
        }
    }

    @Override
    public String getDescription() {
        return "Escena '" + icon + " " + sceneName + "' (" + commands.size() + " acciones coordinadas)";
    }

    public String getSceneName() {
        return sceneName;
    }

    public String getIcon() {
        return icon;
    }

    public List<ICommand> getCommands() {
        return Collections.unmodifiableList(commands);
    }
}
