package io.github.StarvingValley.models.events;

import com.badlogic.ashley.core.Entity;
import io.github.StarvingValley.models.Interfaces.Event;

/**
 * Event triggered when a player attempts to pick up an item.
 */
public class ItemPickupAttemptEvent implements Event {
    public final Entity player;
    public final Entity item;

    public ItemPickupAttemptEvent(Entity player, Entity item) {
        this.player = player;
        this.item = item;
    }
} 