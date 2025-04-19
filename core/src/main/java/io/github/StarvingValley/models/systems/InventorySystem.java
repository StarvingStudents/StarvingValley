package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.events.AddItemToInventoryEvent;
import io.github.StarvingValley.models.events.RemoveItemFromInventoryEvent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.InventoryInfo;
import io.github.StarvingValley.utils.InventoryUtils;

public class InventorySystem extends EntitySystem {
    private final GameContext context;

    public InventorySystem(GameContext context) {
        this.context = context;
    }

    @Override
    public void update(float deltaTime) {
        for (AddItemToInventoryEvent event : context.eventBus.getEvents(AddItemToInventoryEvent.class)) {
            handleAdd(event);
        }

        for (RemoveItemFromInventoryEvent event : context.eventBus.getEvents(RemoveItemFromInventoryEvent.class)) {
            handleRemove(event);
        }
    }

    private void handleAdd(AddItemToInventoryEvent event) {
        Entity owner = event.inventoryOwner;
        boolean added = false;

        InventoryInfo hotbar = Mappers.hotbar.has(owner) ? Mappers.hotbar.get(owner).info : null;
        InventoryInfo inventory = Mappers.inventory.has(owner) ? Mappers.inventory.get(owner).info : null;

        if (hotbar != null && InventoryUtils.hasStackOfType(getEngine(), hotbar.inventoryId, event.itemType)) {
            added = InventoryUtils.addItemToInventory(getEngine(), hotbar, event.itemType, event.quantity,
                    context.eventBus) != null;
        }

        if (!added && inventory != null
                && InventoryUtils.hasStackOfType(getEngine(), inventory.inventoryId, event.itemType)) {
            added = InventoryUtils.addItemToInventory(getEngine(), inventory, event.itemType, event.quantity,
                    context.eventBus) != null;
        }

        if (!added && hotbar != null) {
            added = InventoryUtils.addItemToInventory(getEngine(), hotbar, event.itemType, event.quantity,
                    context.eventBus) != null;
        }

        if (!added && inventory != null) {
            added = InventoryUtils.addItemToInventory(getEngine(), inventory, event.itemType, event.quantity,
                    context.eventBus) != null;
        }
    }

    private void handleRemove(RemoveItemFromInventoryEvent event) {
        Entity owner = event.inventoryOwner;

        boolean changed = false;

        if (Mappers.hotbar.has(owner)) {
            InventoryInfo hotbar = Mappers.hotbar.get(owner).info;
            changed = InventoryUtils.removeItemFromInventory(getEngine(), hotbar.inventoryId, event.itemType,
                    event.quantity, context.eventBus);
        }

        if (!changed && Mappers.inventory.has(owner)) {
            InventoryInfo inventory = Mappers.inventory.get(owner).info;
            changed = InventoryUtils.removeItemFromInventory(getEngine(), inventory.inventoryId, event.itemType,
                    event.quantity, context.eventBus);
        }
    }
}
