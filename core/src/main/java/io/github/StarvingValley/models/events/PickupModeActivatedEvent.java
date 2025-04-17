package io.github.StarvingValley.models.events;

import com.badlogic.ashley.core.Entity;
import io.github.StarvingValley.models.Interfaces.Event;

/**
 * Event triggered when pickup mode is activated.
 */
public class PickupModeActivatedEvent implements Event {
    public final Entity player;

    public PickupModeActivatedEvent(Entity player) {
        this.player = player;
    }
} 