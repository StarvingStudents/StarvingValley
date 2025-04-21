package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

import io.github.StarvingValley.models.types.PrefabType;

public class InventoryItemComponent implements Component {
    public PrefabType type;
    public int quantity;
    public int slotX;
    public int slotY;
    public String inventoryId;

    public InventoryItemComponent(PrefabType type, int quantity, int slotX, int slotY, String inventoryId) {
        this.type = type;
        this.quantity = quantity;
        this.slotX = slotX;
        this.slotY = slotY;
        this.inventoryId = inventoryId;
    }
}
