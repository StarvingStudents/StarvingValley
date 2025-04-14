package io.github.StarvingValley.models.systems;

import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.events.EntityUpdatedEvent;
import io.github.StarvingValley.models.events.ItemDroppedEvent;
import io.github.StarvingValley.models.events.ItemUsedEvent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.Inventory;
import io.github.StarvingValley.models.types.ItemStack;
import io.github.StarvingValley.models.types.PrefabType;
import io.github.StarvingValley.utils.InventoryUtils;

public class InventorySystem extends EntitySystem {
    private final GameContext context;

    public InventorySystem(GameContext context) {
        this.context = context;
    }

    @Override
    public void update(float deltaTime) {
        Entity player = context.player;
        if (player == null || !Mappers.inventory.has(player) || !Mappers.hotbar.has(player))
            return;

        Inventory inventory = Mappers.inventory.get(player).inventory;
        Inventory hotbar = Mappers.hotbar.get(player).hotbar;

        boolean entityUpdated = false;

        List<ItemDroppedEvent> itemsDroppedEvents = context.eventBus.getEvents(ItemDroppedEvent.class);
        for (ItemDroppedEvent itemDroppedEvent : itemsDroppedEvents) {
            boolean added = addItems(itemDroppedEvent.itemDrop, hotbar, inventory);

            if (!added) {
                System.out.println("Inventory and hotbar full, could not add " + itemDroppedEvent.itemDrop.type);
            } else {
                entityUpdated = true;
            }
        }

        List<ItemUsedEvent> itemsUsedEvents = context.eventBus.getEvents(ItemUsedEvent.class);
        for (ItemUsedEvent itemUsedEvent : itemsUsedEvents) {
            boolean removed = removeItems(itemUsedEvent.itemStack, hotbar, inventory);

            if (!removed) {
                System.out.println(
                        "Could not remove " + itemUsedEvent.itemStack.quantity + " of " + itemUsedEvent.itemStack.type);
            } else {
                entityUpdated = true;
            }
        }

        if (entityUpdated) {
            System.out.println("Hotbar:");
            hotbar.printInventory();

            System.out.println("Inventory:");
            inventory.printInventory();

            context.eventBus.publish(new EntityUpdatedEvent(player));

            if (InventoryUtils.isInventoryOpen(getEngine())) {
                InventoryUtils.closeInventory(getEngine());
                InventoryUtils.addInventoryToEngine(getEngine(), inventory);
            }
        }
    }

    private boolean addItems(ItemStack stack, Inventory hotbar, Inventory inventory) {
        PrefabType type = stack.type;
        int quantity = stack.quantity;

        // TODO: remove this and uncomment below when added hotbar, this is just for
        // testing
        boolean inventoryHasType = inventory.hasStackOfType(type);
        if (inventoryHasType) {
            inventory.addItem(type, quantity);
            return true;
        }
        return inventory.addItem(type, quantity);

        // boolean hotbarHasType = hotbar.hasStackOfType(type);
        // if (hotbarHasType) {
        // hotbar.addItem(type, quantity);
        // return true;
        // }

        // inventoryHasType = inventory.hasStackOfType(type);
        // if (inventoryHasType) {
        // inventory.addItem(type, quantity);
        // return true;
        // }

        // boolean added = hotbar.addItem(type, quantity);
        // if (!added) {
        // added = inventory.addItem(type, quantity);
        // }

        // return added;
    }

    private boolean removeItems(ItemStack stack, Inventory hotbar, Inventory inventory) {
        boolean removed = hotbar.removeItem(stack.type, stack.quantity);

        if (!removed) {
            removed = inventory.removeItem(stack.type, stack.quantity);
        }

        return removed;
    }
}
