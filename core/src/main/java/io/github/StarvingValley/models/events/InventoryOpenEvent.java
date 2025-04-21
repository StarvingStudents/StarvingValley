package io.github.StarvingValley.models.events;

import io.github.StarvingValley.models.interfaces.Event;
import io.github.StarvingValley.models.types.InventoryInfo;

public class InventoryOpenEvent implements Event {
    public InventoryInfo inventory;

    public InventoryOpenEvent(InventoryInfo inventory) {
        this.inventory = inventory;
    }
}
