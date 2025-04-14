package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

import io.github.StarvingValley.models.types.PrefabType;

public class InventoryItemComponent implements Component {
    public PrefabType type;
    public int quantity;
    public int slotX;
    public int slotY;

    public InventoryItemComponent(PrefabType type, int quantity, int slotX, int slotY) {
        this.type = type;
        this.quantity = quantity;
        this.slotX = slotX;
        this.slotY = slotY;
    }
}
