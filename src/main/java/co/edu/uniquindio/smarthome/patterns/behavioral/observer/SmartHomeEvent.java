package co.edu.uniquindio.smarthome.patterns.behavioral.observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SmartHomeEvent {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final EventType type;
    private final String source;
    private final String description;
    private final String severity; // "INFO", "WARNING", "DANGER", "SUCCESS"
    private final LocalDateTime timestamp;

    public SmartHomeEvent(EventType type, String source, String description, String severity) {
        this.type = type;
        this.source = source;
        this.description = description;
        this.severity = severity;
        this.timestamp = LocalDateTime.now();
    }

    public EventType getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public String getDescription() {
        return description;
    }

    public String getSeverity() {
        return severity;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getFormattedTime() {
        return timestamp.format(FORMATTER);
    }

    @Override
    public String toString() {
        return "[" + getFormattedTime() + "] " + type.getIcon() + " (" + source + "): " + description;
    }
}
