package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

import java.util.ArrayList;
import java.util.List;

public class InventoryComponent implements Component {
    private List<String> items; // Using String for simplicity initially, will represent item IDs or names

    public InventoryComponent() {
        this.items = new ArrayList<>();
    }

    public List<String> getItems() {
        return items;
    }

    public void addItem(String item) {
        items.add(item);
    }

    public void removeItem(String item) {
        items.remove(item);
    }

    public boolean hasItem(String item) {
        return items.contains(item);
    }
}
