package io.github.StarvingValley.models.events;

import io.github.StarvingValley.models.Interfaces.Event;
import io.github.StarvingValley.models.types.ItemStack;

public class RemoveItemFromInventoryEvent implements Event {
	public ItemStack itemStack;

	public RemoveItemFromInventoryEvent(ItemStack itemStack) {
		this.itemStack = itemStack;
	}
}
