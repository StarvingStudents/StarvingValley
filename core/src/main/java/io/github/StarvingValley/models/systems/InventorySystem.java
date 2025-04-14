package io.github.StarvingValley.models.systems;

import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.InventoryToggleButtonComponent;
import io.github.StarvingValley.models.events.EntityUpdatedEvent;
import io.github.StarvingValley.models.events.InventoryCloseEvent;
import io.github.StarvingValley.models.events.InventoryOpenEvent;
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
        if (player == null || !Mappers.inventory.has(player))
            return;

        handleToggleEvents(player);

        if (!Mappers.hotbar.has(player))
            return;

        Inventory inventory = Mappers.inventory.get(player).inventory;
        Inventory hotbar = Mappers.hotbar.get(player).hotbar;

        ChangeResult result = new ChangeResult();

        List<ItemDroppedEvent> itemsDroppedEvents = context.eventBus.getEvents(ItemDroppedEvent.class);
        for (ItemDroppedEvent itemDroppedEvent : itemsDroppedEvents) {
            ChangeResult addResult = addItems(itemDroppedEvent.itemDrop, hotbar, inventory);

            if (!addResult.changedHotbar && !addResult.changedInventory) {
                System.out.println("Inventory and hotbar full, could not add " + itemDroppedEvent.itemDrop.type);
            } else {
                result.changedHotbar = addResult.changedHotbar;
                result.changedInventory = addResult.changedInventory;
            }
        }

        List<ItemUsedEvent> itemsUsedEvents = context.eventBus.getEvents(ItemUsedEvent.class);
        for (ItemUsedEvent itemUsedEvent : itemsUsedEvents) {
            ChangeResult removeResult = removeItems(itemUsedEvent.itemStack, hotbar, inventory);

            if (!removeResult.changedHotbar && !removeResult.changedInventory) {
                System.out.println(
                        "Could not remove " + itemUsedEvent.itemStack.quantity + " of " + itemUsedEvent.itemStack.type);
            } else {
                result.changedHotbar = removeResult.changedHotbar;
                result.changedInventory = removeResult.changedInventory;
            }
        }

        if (result.changedInventory) {
            context.eventBus.publish(new EntityUpdatedEvent(player));

            if (InventoryUtils.isInventoryOpen(getEngine())) {
                InventoryUtils.closeInventory(getEngine());
                InventoryUtils.addInventoryToEngine(getEngine(), inventory);
            }
        } else if (result.changedHotbar) {
            context.eventBus.publish(new EntityUpdatedEvent(player));

            if (InventoryUtils.isHotbarOpen(getEngine())) {
                InventoryUtils.closeHotbar(getEngine());
                InventoryUtils.addHotbarToEngine(getEngine(), hotbar);
            }
        }
    }

    private void handleToggleEvents(Entity player) {
        Inventory inventory = Mappers.inventory.get(player).inventory;

        ImmutableArray<Entity> clickedToggleButtons = getEngine()
                .getEntitiesFor(Family.all(InventoryToggleButtonComponent.class, ClickedComponent.class).get());
        for (int i = 0; i < clickedToggleButtons.size(); i++) {
            InventoryUtils.toggleInventory(getEngine(), inventory);
        }

        List<InventoryOpenEvent> openEvents = context.eventBus.getEvents(InventoryOpenEvent.class);
        if (!openEvents.isEmpty()) {
            InventoryUtils.addInventoryToEngine(getEngine(), inventory);
        }

        List<InventoryCloseEvent> closeEvents = context.eventBus.getEvents(InventoryCloseEvent.class);
        if (!closeEvents.isEmpty()) {
            InventoryUtils.closeInventory(getEngine());
        }
    }

    private ChangeResult addItems(ItemStack stack, Inventory hotbar, Inventory inventory) {
        PrefabType type = stack.type;
        int quantity = stack.quantity;

        ChangeResult result = new ChangeResult();

        boolean hotbarHasType = hotbar.hasStackOfType(type);
        if (hotbarHasType) {
            hotbar.addItem(type, quantity);
            result.changedHotbar = true;
            return result;
        }
        boolean inventoryHasType = inventory.hasStackOfType(type);
        inventoryHasType = inventory.hasStackOfType(type);
        if (inventoryHasType) {
            inventory.addItem(type, quantity);
            result.changedInventory = true;
            return result;
        }

        result.changedHotbar = hotbar.addItem(type, quantity);
        if (!result.changedHotbar) {
            result.changedInventory = inventory.addItem(type, quantity);
        }

        return result;
    }

    private ChangeResult removeItems(ItemStack stack, Inventory hotbar, Inventory inventory) {
        ChangeResult result = new ChangeResult();
        result.changedHotbar = hotbar.removeItem(stack.type, stack.quantity);

        if (!result.changedHotbar) {
            result.changedInventory = inventory.removeItem(stack.type, stack.quantity);
        }

        return result;
    }

    private class ChangeResult {
        public boolean changedInventory = false;
        public boolean changedHotbar = false;
    }
}
