package io.github.StarvingValley.models.types;

public class ItemTrade {
    public PrefabType type;
    public int price;

    public ItemTrade(PrefabType type, int price) {
        this.type = type;
        this.price = price;
    }

    public ItemTrade() {

    }
}
