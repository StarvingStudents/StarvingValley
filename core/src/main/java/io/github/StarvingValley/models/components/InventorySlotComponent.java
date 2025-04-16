package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class InventorySlotComponent implements Component {
    public final int slotX;
    public final int slotY;

    public InventorySlotComponent(int slotX, int slotY) {
        this.slotX = slotX;
        this.slotY = slotY;
    }
}
