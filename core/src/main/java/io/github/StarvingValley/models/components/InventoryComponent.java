package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

import io.github.StarvingValley.models.types.InventoryInfo;

public class InventoryComponent implements Component {
    public InventoryInfo info;

    public InventoryComponent(InventoryInfo info) {
        this.info = info;
    }
}
