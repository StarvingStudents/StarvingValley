package io.github.StarvingValley.models.events;

import io.github.StarvingValley.models.Interfaces.Event;
import io.github.StarvingValley.models.types.ItemStack;

public class ItemDroppedEvent implements Event {
    public ItemStack itemDrop;

	public ItemDroppedEvent(ItemStack itemDrop) {
		this.itemDrop = itemDrop;
	}
}
