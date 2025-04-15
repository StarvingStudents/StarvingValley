package io.github.StarvingValley.models.components;

import java.util.List;

import com.badlogic.ashley.core.Component;

import io.github.StarvingValley.models.types.Inventory;
import io.github.StarvingValley.models.types.InventorySlot;

public class InventoryComponent implements Component {
    public Inventory inventory;

    public InventoryComponent() {
        inventory = new Inventory();
        inventory.width = 6;
        inventory.height = 3;
    }

    public InventoryComponent(List<InventorySlot> slots, int width, int height) {
        inventory = new Inventory(slots, width, height);
    }
}
