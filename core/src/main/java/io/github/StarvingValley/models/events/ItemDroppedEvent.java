package io.github.StarvingValley.models.events;

import io.github.StarvingValley.models.Interfaces.Event;
import io.github.StarvingValley.models.types.ItemDrop;

public class ItemDroppedEvent implements Event {
    public ItemDrop itemDrop;

	public ItemDroppedEvent(ItemDrop itemDrop) {
		this.itemDrop = itemDrop;
	}
}
