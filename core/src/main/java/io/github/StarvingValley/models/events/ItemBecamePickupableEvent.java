package io.github.StarvingValley.models.events;

import com.badlogic.ashley.core.Entity;
import io.github.StarvingValley.models.Interfaces.Event;

/**
 * Event triggered when an entity becomes available for pickup.
 */
public class ItemBecamePickupableEvent implements Event {
    public final Entity item;

    public ItemBecamePickupableEvent(Entity item) {
        this.item = item;
    }
} 