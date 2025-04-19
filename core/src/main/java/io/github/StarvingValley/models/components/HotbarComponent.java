package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

import io.github.StarvingValley.models.types.InventoryInfo;

public class HotbarComponent implements Component {
    public InventoryInfo info;

    public HotbarComponent(InventoryInfo info) {
        this.info = info;
    }
}
