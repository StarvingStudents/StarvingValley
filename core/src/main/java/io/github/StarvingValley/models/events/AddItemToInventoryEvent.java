package io.github.StarvingValley.models.events;

import io.github.StarvingValley.models.Interfaces.Event;
import io.github.StarvingValley.models.types.ItemStack;

public class AddItemToInventoryEvent implements Event {
	public ItemStack itemStack;

	public AddItemToInventoryEvent(ItemStack itemStack) {
		this.itemStack = itemStack;
	}
}
