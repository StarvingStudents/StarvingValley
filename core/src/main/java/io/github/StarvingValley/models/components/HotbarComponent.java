package io.github.StarvingValley.models.components;

import java.util.List;

import com.badlogic.ashley.core.Component;

import io.github.StarvingValley.models.types.Inventory;
import io.github.StarvingValley.models.types.InventorySlot;

public class HotbarComponent implements Component {
    public Inventory hotbar;

    public HotbarComponent() {
        hotbar = new Inventory();
        hotbar.width = 6;
        hotbar.height = 1;
    }

    public HotbarComponent(List<InventorySlot> slots, int width, int height) {
        hotbar = new Inventory(slots, width, height);
    }

    public HotbarComponent(Inventory hotbar) {
        this.hotbar = hotbar;
    }
}
