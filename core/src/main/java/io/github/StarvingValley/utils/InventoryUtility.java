package io.github.StarvingValley.utils;

import io.github.StarvingValley.models.components.InventoryComponent;
import com.badlogic.ashley.core.Entity;

public class InventoryUtility {

    /**
     * Adds an item to the player's inventory.
     *
     * @param player The player entity.
     * @param itemId The unique ID or identifier of the item.
     * @return true if the item was successfully added.
     */
    public static boolean addItem(Entity player, String itemId) {
        InventoryComponent inv = (InventoryComponent) player.getComponent(InventoryComponent.class);
        if (inv == null) return false;
        inv.addItem(itemId);
        return true;
    }

    /**
     * Removes an item from the player's inventory.
     *
     * @param player The player entity.
     * @param itemId The unique ID or identifier of the item.
     * @return true if the item was successfully removed.
     */
    public static boolean removeItem(Entity player, String itemId) {
        InventoryComponent inv = (InventoryComponent) player.getComponent(InventoryComponent.class);
        if (inv == null || !inv.hasItem(itemId)) return false;
        inv.removeItem(itemId);
        return true;
    }
}
