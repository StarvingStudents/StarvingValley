package io.github.StarvingValley.models.events;

import java.time.Duration;
import java.time.Instant;

import io.github.StarvingValley.models.Interfaces.Event;

public class NotificationEvent implements Event {
    public String message;
    public Instant expiryTime;

    public NotificationEvent(String message) {
        this.message = message;
        this.expiryTime = Instant.now().plus(Duration.ofSeconds(5));
    }

    public NotificationEvent(String message, Duration displayDuration) {
        this.message = message;
        this.expiryTime = Instant.now().plus(displayDuration);
    }
}
