package io.github.StarvingValley.models.events;

import io.github.StarvingValley.models.Interfaces.Event;
import io.github.StarvingValley.models.types.ItemStack;

//TODO: Rename to clearer name
public class ItemUsedEvent implements Event {
	public ItemStack itemStack;

	public ItemUsedEvent(ItemStack itemDrop) {
		this.itemStack = itemDrop;
	}
}
