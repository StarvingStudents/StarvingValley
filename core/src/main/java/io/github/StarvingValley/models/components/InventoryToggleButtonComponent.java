package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

import io.github.StarvingValley.models.types.InventoryInfo;

public class InventoryToggleButtonComponent implements Component {
    public InventoryInfo inventoryToToggle;

    public InventoryToggleButtonComponent(InventoryInfo inventoryToToggle) {
        this.inventoryToToggle = inventoryToToggle;
    }
}
