package io.github.StarvingValley.models.types;

public class ItemDrop {
    public PrefabType type;
    public int count;

    public ItemDrop(PrefabType type, int count) {
        this.type = type;
        this.count = count;
    }

    public ItemDrop() {

    }
}
