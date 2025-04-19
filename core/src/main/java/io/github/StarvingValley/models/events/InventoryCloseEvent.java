package io.github.StarvingValley.models.events;

import io.github.StarvingValley.models.Interfaces.Event;
import io.github.StarvingValley.models.types.InventoryInfo;

public class InventoryCloseEvent implements Event {
    public InventoryInfo inventory;

    public InventoryCloseEvent(InventoryInfo inventory) {
        this.inventory = inventory;
    }
}