package io.github.StarvingValley.models.events;

import io.github.StarvingValley.models.Interfaces.Event;
import io.github.StarvingValley.models.types.InventoryInfo;

public class InventoryOpenEvent implements Event {
    public InventoryInfo inventory;
    public boolean isHotbar;

    public InventoryOpenEvent(InventoryInfo inventory, boolean isHotbar) {
        this.inventory = inventory;
        this.isHotbar = isHotbar;
    }
}
