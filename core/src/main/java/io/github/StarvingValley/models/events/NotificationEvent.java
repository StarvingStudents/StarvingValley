package io.github.StarvingValley.models.events;

import java.time.Duration;

import io.github.StarvingValley.models.Interfaces.Event;

public class NotificationEvent implements Event {
    public String message;
    public Duration displayDuration;

    public NotificationEvent(String message) {
        this.message = message;
        this.displayDuration = Duration.ofSeconds(5);
    }

    public NotificationEvent(String message, Duration displayDuration) {
        this.message = message;
        this.displayDuration = displayDuration;
    }
}
