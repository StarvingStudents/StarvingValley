package io.github.StarvingValley.models.events;

import com.badlogic.ashley.core.Entity;
import io.github.StarvingValley.models.Interfaces.Event;

/**
 * Event triggered when pickup mode is deactivated.
 */
public class PickupModeDeactivatedEvent implements Event {
    public final Entity player;
    public final DeactivationReason reason;

    public enum DeactivationReason {
        MANUAL,
        BUILD_MODE_ACTIVATED,
        DISTANCE,
        TIMEOUT,
        ITEM_PICKED
    }

    public PickupModeDeactivatedEvent(Entity player, DeactivationReason reason) {
        this.player = player;
        this.reason = reason;
    }
} 