package io.github.StarvingValley.models.types;

public class InventorySlot {
    public ItemStack itemStack;
    public int x;
    public int y;

    public InventorySlot(PrefabType type, int quantity, int x, int y) {
        itemStack = new ItemStack(type, quantity);
        this.x = x;
        this.y = y;
    }

    public InventorySlot() {

    }

    public PrefabType getType() {
        return itemStack != null ? itemStack.type : null;
    }

    public int getQuantity() {
        return itemStack != null ? itemStack.quantity : null;
    }
}
