package io.github.StarvingValley.models.types;

import java.util.UUID;

public class InventoryInfo {
    public String inventoryId;
    public int width, height;
    public float startX, startY;
    public InventoryType inventoryType;
    public boolean isOpen;

    public InventoryInfo() {
    }

    public InventoryInfo(String inventoryId, int width, int height, InventoryType type) {
        this.inventoryId = inventoryId;
        this.width = width;
        this.height = height;
        this.inventoryType = type;
    }

    public InventoryInfo(int width, int height, InventoryType type) {
        this.width = width;
        this.height = height;
        this.inventoryType = type;
        inventoryId = UUID.randomUUID().toString();
    }
}
