package io.github.StarvingValley.models.types;

public class ItemTrade {
    public PrefabType type;
    public int cost;

    public ItemTrade(PrefabType type, int cost) {
        this.type = type;
        this.cost = cost;
    }

    public ItemTrade() {

    }
}
