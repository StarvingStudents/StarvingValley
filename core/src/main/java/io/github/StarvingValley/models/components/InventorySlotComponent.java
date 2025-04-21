package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class InventorySlotComponent implements Component {
    public final int slotX;
    public final int slotY;
    public String inventoryId;

    public InventorySlotComponent(int slotX, int slotY, String inventoryId) {
        this.slotX = slotX;
        this.slotY = slotY;
        this.inventoryId = inventoryId;
    }
}
