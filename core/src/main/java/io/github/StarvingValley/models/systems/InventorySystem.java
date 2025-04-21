package io.github.StarvingValley.models.systems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.events.AddItemToInventoryEvent;
import io.github.StarvingValley.models.events.RemoveItemFromInventoryEvent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.InventoryInfo;
import io.github.StarvingValley.models.types.ItemStack;
import io.github.StarvingValley.utils.InventoryUtils;

public class InventorySystem extends EntitySystem {
    private final GameContext context;

    public InventorySystem(GameContext context) {
        this.context = context;
    }

    @Override
    public void update(float deltaTime) {
        List<AddItemToInventoryEvent> addEvents = context.eventBus.getEvents(AddItemToInventoryEvent.class);
        Map<Entity, List<ItemStack>> groupedStacksToAdd = new HashMap<>();

        for (AddItemToInventoryEvent addEvent : addEvents) {
            groupedStacksToAdd
                    .computeIfAbsent(addEvent.inventoryOwner, k -> new ArrayList<>())
                    .add(new ItemStack(addEvent.itemType, addEvent.quantity));
        }

        for (Map.Entry<Entity, List<ItemStack>> entry : groupedStacksToAdd.entrySet()) {
            handleAdd(entry.getKey(), entry.getValue());
        }

        for (RemoveItemFromInventoryEvent event : context.eventBus.getEvents(RemoveItemFromInventoryEvent.class)) {
            handleRemove(event);
        }
    }

    private void handleAdd(Entity owner, List<ItemStack> itemsToAdd) {
        if (itemsToAdd.isEmpty())
            return;

        InventoryInfo hotbar = Mappers.hotbar.has(owner) ? Mappers.hotbar.get(owner).info : null;
        InventoryInfo inventory = Mappers.inventory.has(owner) ? Mappers.inventory.get(owner).info : null;

        List<ItemStack> remaining = itemsToAdd;

        if (hotbar != null) {
            remaining = InventoryUtils.addToExistingStacks(getEngine(), hotbar, remaining, context.eventBus);
        }

        if (!remaining.isEmpty() && inventory != null) {
            remaining = InventoryUtils.addToExistingStacks(getEngine(), inventory, remaining, context.eventBus);
        }

        if (!remaining.isEmpty() && hotbar != null) {
            remaining = InventoryUtils.addItemsToInventory(getEngine(), hotbar, remaining, context.eventBus);
        }

        if (!remaining.isEmpty() && inventory != null) {
            remaining = InventoryUtils.addItemsToInventory(getEngine(), inventory, remaining, context.eventBus);
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
