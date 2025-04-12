package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

import io.github.StarvingValley.models.types.InventorySlot;

//TODO: Maybe change all inventory-related components to be flat
public class InventoryItemComponent implements Component {
    public InventorySlot inventorySlot;

    public InventoryItemComponent(InventorySlot inventorySlot) {
        this.inventorySlot = inventorySlot;
    }
}
