package io.github.StarvingValley.models.types;

public class ItemStack {
    public PrefabType type;
    public int quantity;

    public ItemStack(PrefabType type, int quantity) {
        this.type = type;
        this.quantity = quantity;
    }

    public ItemStack() {

    }
}
