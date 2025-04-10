package io.github.StarvingValley.models.components;

import static io.github.StarvingValley.models.systems.InventorySystem.MAX_INVENTORY_CAPACITY;

import com.badlogic.ashley.core.Component;
import java.util.HashMap;
import java.util.Map;

public class InventoryComponent implements Component {
    // Map to store item IDs and their quantities
    private Map<String, Integer> items;

    public InventoryComponent() {
        this.items = new HashMap<>();
    }

    // Getter to access the entire map
    public Map<String, Integer> getItems() {
        return items;
    }

    // Adds an item: if it already exists, increments its quantity, otherwise sets it to 1
    public void addItem(String item){
        if(!isFull() || hasItem(item)){
            items.put(item, items.getOrDefault(item, 0) + 1);
        }
    }

    // Removes one unit of the specified item: if the quantity becomes 0, the item is removed from the map
    public void removeItem(String item) {
        if (items.containsKey(item)) {
            int count = items.get(item);
            if (count > 1) {
                items.put(item, count - 1);
            } else {
                items.remove(item);
            }
        }
    }

    // Checks if the item exists in the inventory
    public boolean hasItem(String item) {
        return items.containsKey(item);
    }

    public boolean isFull() {
        return items.size() >= MAX_INVENTORY_CAPACITY;
    }

    // Gets the quantity of a specific item, or 0 if the item doesn't exist
    public int getItemQuantity(String item) {
        return items.getOrDefault(item, 0);
    }

    // Clears all items from the inventory
    public void clearInventory() {
        items.clear();
    }

}
