package io.github.StarvingValley.models.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.StarvingValley.models.interfaces.Event;
import io.github.StarvingValley.utils.EventDebugger;

/**
 * Publish-Subscribe event bus storing current and next frame's events. To avoid
 * some systems not getting events or some getting an event twice, events are
 * published into the <b>current</b> bus and won't be available to subscribers
 * until
 * the next frame.
 * <b>Subscribers will therefore always be 1 frame behind</b>
 */
public class EventBus {
    private Map<Class<? extends Event>, List<Event>> currentFrameEvents = new HashMap<>();
    private Map<Class<? extends Event>, List<Event>> nextFrameEvents = new HashMap<>();
    private EventDebugger debugger;

    public EventBus(EventDebugger debugger) {
        super();
        this.debugger = debugger;
    }

    /**
     * Publishes an event to be processed in the next frame
     * 
     * @param event
     */
    public void publish(Event event) {
        nextFrameEvents.computeIfAbsent(event.getClass(), k -> new ArrayList<>()).add(event);

        debugger.logEvent(event);
    }

    /**
     * Returns the events of the given type for the current frame
     * 
     * @param <T>
     * @param eventType
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends Event> List<T> getEvents(Class<T> eventType) {
        return (List<T>) currentFrameEvents.getOrDefault(eventType, Collections.emptyList());
    }

    /**
     * Moves next frame queue events to current queue and clears current queue
     */
    public void advanceFrame() {
        currentFrameEvents.clear();
        Map<Class<? extends Event>, List<Event>> temp = currentFrameEvents;
        currentFrameEvents = nextFrameEvents;
        nextFrameEvents = temp;
    }
}
