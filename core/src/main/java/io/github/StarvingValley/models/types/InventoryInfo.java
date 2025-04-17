package io.github.StarvingValley.models.types;

import java.util.UUID;

public class InventoryInfo {
    public String inventoryId;
    public int width, height;
    public float startX, startY;
    public boolean isHotbar;

    public InventoryInfo() {
    }

    public InventoryInfo(String inventoryId, int width, int height) {
        this.inventoryId = inventoryId;
        this.width = width;
        this.height = height;
    }

    public InventoryInfo(int width, int height) {
        this.width = width;
        this.height = height;
        inventoryId = UUID.randomUUID().toString();
    }
}
