package io.github.StarvingValley.models.events;

import com.badlogic.ashley.core.Entity;

import io.github.StarvingValley.models.interfaces.Event;
import io.github.StarvingValley.models.types.PrefabType;

public class AddItemToInventoryEvent implements Event {
	public Entity inventoryOwner;
	public PrefabType itemType;
	public int quantity;

	public AddItemToInventoryEvent(Entity inventoryOwner, PrefabType itemType, int quantity) {
		this.inventoryOwner = inventoryOwner;
		this.itemType = itemType;
		this.quantity = quantity;
	}
}
