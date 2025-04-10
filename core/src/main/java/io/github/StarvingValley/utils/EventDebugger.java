package io.github.StarvingValley.utils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import io.github.StarvingValley.models.Interfaces.Event;
import io.github.StarvingValley.models.events.EntityUpdatedEvent;

public class EventDebugger {
    private static final int MAX_EVENTS = 10;
    private final Deque<String> recentEvents = new ArrayDeque<>();

    public void logEvent(Event event) {
        if (event.getClass() == EntityUpdatedEvent.class)
            return;
        if (recentEvents.size() >= MAX_EVENTS) {
            recentEvents.removeFirst();
        }
        recentEvents.addLast(event.getClass().getSimpleName());
    }

    public List<String> getRecentEvents() {
        return new ArrayList<>(recentEvents);
    }
}
