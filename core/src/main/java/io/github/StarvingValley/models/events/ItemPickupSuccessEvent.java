package io.github.StarvingValley.models.events;

import com.badlogic.ashley.core.Entity;
import io.github.StarvingValley.models.Interfaces.Event;

/**
 * Event triggered when an item is successfully picked up.
 */
public class ItemPickupSuccessEvent implements Event {
    public final Entity player;
    public final Entity item;
    public final String itemName;

    public ItemPickupSuccessEvent(Entity player, Entity item, String itemName) {
        this.player = player;
        this.item = item;
        this.itemName = itemName;
    }
} 