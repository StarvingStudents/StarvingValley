package io.github.StarvingValley.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.DraggableComponent;
import io.github.StarvingValley.models.components.HotbarComponent;
import io.github.StarvingValley.models.components.InventoryComponent;
import io.github.StarvingValley.models.components.InventoryItemComponent;
import io.github.StarvingValley.models.components.InventorySlotComponent;
import io.github.StarvingValley.models.components.PartOfHotbarComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SelectedHotbarEntryComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.SyncComponent;
import io.github.StarvingValley.models.components.TextComponent;
import io.github.StarvingValley.models.components.TradeableComponent;
import io.github.StarvingValley.models.entities.InventoryFactory;
import io.github.StarvingValley.models.events.EntityAddedEvent;
import io.github.StarvingValley.models.events.EntityRemovedEvent;
import io.github.StarvingValley.models.events.EntityUpdatedEvent;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.types.InventoryInfo;
import io.github.StarvingValley.models.types.InventoryType;
import io.github.StarvingValley.models.types.ItemStack;
import io.github.StarvingValley.models.types.ItemTrade;
import io.github.StarvingValley.models.types.PrefabType;

public class InventoryUtils {
    public static void openInventory(Engine engine, InventoryInfo inventory, InventoryType inventoryType) {
        closeAllInventories(engine);

        float slotSize = getSlotSize();

        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        float totalInventoryWidth = inventory.width * slotSize;
        float totalInventoryHeight = inventory.height * slotSize;
        float startX = (screenWidth - totalInventoryWidth) / 2f;
        float startY;
        if (inventoryType == InventoryType.HOTBAR)
            startY = getHotbarStartY();
        else
            startY = screenHeight / 2 - totalInventoryHeight / 2 + (slotSize / 4);

        inventory.startX = startX;
        inventory.startY = startY;

        Map<GridPoint2, Entity> itemsBySlot = getItemMap(engine, inventory.inventoryId);

        for (int y = 0; y < inventory.height; y++) {
            for (int x = 0; x < inventory.width; x++) {
                float posX = startX + x * slotSize;
                float posY = startY + (inventory.height - 1 - y) * slotSize;

                Entity backgroundEntity = InventoryFactory.createInventorySlot(
                        posX, posY, x, y, slotSize, inventory.inventoryId);

                if (inventoryType == InventoryType.HOTBAR) {
                    backgroundEntity.add(new PartOfHotbarComponent());
                }

                engine.addEntity(backgroundEntity);

                Entity item = itemsBySlot.get(new GridPoint2(x, y));
                if (item != null) {
                    InventoryItemComponent itemComponent = Mappers.inventoryItem.get(item);
                    if (itemComponent != null && itemComponent.quantity > 0) {
                        Vector2 itemPos = applyItemSizeToSlotPosition(posX, posY);
                        item.add(new PositionComponent(itemPos.x, itemPos.y));
                    }
                }
            }
        }

        inventory.inventoryType = inventoryType;
        if (inventoryType == InventoryType.INVENTORY) {
            enableInventoryItemDragging(engine);
        }

        inventory.isOpen = true;
    }

    public static void closeInventory(Engine engine, InventoryInfo inventory) {
        List<Entity> entitiesToRemove = new ArrayList<>();

        for (Entity slot : engine.getEntitiesFor(Family.all(InventorySlotComponent.class).get())) {
            InventorySlotComponent slotComponent = Mappers.inventorySlot.get(slot);
            if (slotComponent.inventoryId.equals(inventory.inventoryId)) {
                entitiesToRemove.add(slot);
            }
        }

        for (Entity e : entitiesToRemove) {
            engine.removeEntity(e);
        }

        for (Entity item : engine.getEntitiesFor(Family.all(InventoryItemComponent.class).get())) {
            InventoryItemComponent itemComponent = Mappers.inventoryItem.get(item);
            if (itemComponent.inventoryId.equals(inventory.inventoryId)) {
                item.remove(PositionComponent.class);
            }
        }

        if (inventory.inventoryType == InventoryType.INVENTORY) {
            disableInventoryItemDragging(engine);
        }

        inventory.isOpen = false;
    }

    public static void closeAllInventories(Engine engine) {
        engine.removeAllEntities(Family.all(InventorySlotComponent.class).exclude(PartOfHotbarComponent.class).get());

        for (Entity item : engine
                .getEntitiesFor(Family.all(InventoryItemComponent.class).exclude(PartOfHotbarComponent.class).get())) {
            item.remove(PositionComponent.class);
        }

        disableInventoryItemDragging(engine);

        for (Entity entity : engine
                .getEntitiesFor(Family.all(InventoryComponent.class).get())) {
            InventoryComponent inventory = Mappers.inventory.get(entity);
            inventory.info.isOpen = false;
        }
    }

    public static List<Entity> getItemsForInventory(Engine engine, String inventoryId) {
        List<Entity> items = new ArrayList<Entity>();

        for (Entity e : engine.getEntitiesFor(Family.all(InventoryItemComponent.class).get())) {
            InventoryItemComponent item = e.getComponent(InventoryItemComponent.class);
            if (item.inventoryId.equals(inventoryId)) {
                items.add(e);
            }
        }

        return items;
    }

    public static Map<GridPoint2, Entity> getItemMap(Engine engine, String inventoryId) {
        Map<GridPoint2, Entity> map = new HashMap<>();

        for (Entity e : engine.getEntitiesFor(Family.all(InventoryItemComponent.class).get())) {
            InventoryItemComponent item = e.getComponent(InventoryItemComponent.class);
            if (item.inventoryId.equals(inventoryId)) {
                GridPoint2 key = new GridPoint2(item.slotX, item.slotY);
                map.put(key, e);
            }
        }

        return map;
    }

    public static List<Entity> getSlotsForInventory(Engine engine, String inventoryId) {
        List<Entity> slots = new ArrayList<Entity>();

        for (Entity e : engine.getEntitiesFor(Family.all(InventorySlotComponent.class).get())) {
            InventorySlotComponent slot = e.getComponent(InventorySlotComponent.class);
            if (slot.inventoryId.equals(inventoryId)) {
                slots.add(e);
            }
        }

        return slots;
    }

    public static Entity getSlot(Engine engine, String inventoryId, int slotX, int slotY) {
        for (Entity entity : engine.getEntitiesFor(Family.all(InventorySlotComponent.class).get())) {
            InventorySlotComponent slot = entity.getComponent(InventorySlotComponent.class);
            if (slot.inventoryId.equals(inventoryId) && slot.slotX == slotX && slot.slotY == slotY) {
                return entity;
            }
        }

        return null;
    }

    public static Entity getItemAt(Engine engine, String inventoryId, int slotX, int slotY) {
        for (Entity entity : engine
                .getEntitiesFor(Family.all(InventoryItemComponent.class, PositionComponent.class).get())) {
            InventoryItemComponent item = Mappers.inventoryItem.get(entity);
            if (item.inventoryId.equals(inventoryId) && item.slotX == slotX && item.slotY == slotY) {
                return entity;
            }
        }
        return null;
    }

    public static Entity addItemToInventory(
            Engine engine,
            InventoryInfo inventory,
            PrefabType type,
            int quantity, EventBus eventBus) {

        List<Entity> items = getItemsForInventory(engine, inventory.inventoryId);

        for (Entity e : items) {
            InventoryItemComponent item = Mappers.inventoryItem.get(e);
            if (item.type.equals(type)) {
                setItemQuantity(e, item.quantity + quantity);
                eventBus.publish(new EntityUpdatedEvent(e));
                return e;
            }
        }

        Set<GridPoint2> occupied = new HashSet<>();
        for (Entity e : items) {
            InventoryItemComponent item = Mappers.inventoryItem.get(e);
            occupied.add(new GridPoint2(item.slotX, item.slotY));
        }

        float slotSize = getSlotSize();
        float itemSize = getItemSize();

        for (int y = 0; y < inventory.height; y++) {
            for (int x = 0; x < inventory.width; x++) {
                GridPoint2 pos = new GridPoint2(x, y);
                if (!occupied.contains(pos)) {
                    Entity item = InventoryFactory.createInventoryItem(
                            new InventoryItemComponent(type, quantity, x, y, inventory.inventoryId),
                            (int) (itemSize),
                                    inventory.inventoryId);

                    if (inventory.inventoryType == InventoryType.HOTBAR) {
                        item.add(new PartOfHotbarComponent());
                    }

                    if (inventory.isOpen) {
                        float posX = inventory.startX + x * slotSize;
                        float posY = inventory.startY + (inventory.height - 1 - y) * slotSize;

                        Vector2 itemPos = applyItemSizeToSlotPosition(posX, posY);

                        item.add(new PositionComponent(itemPos.x, itemPos.y));
                    }
                    engine.addEntity(item);
                    eventBus.publish(new EntityAddedEvent(item));
                    return item;
                }
            }
        }

        return null;
    }

    public static boolean removeItemFromInventory(
            Engine engine,
            String inventoryId,
            PrefabType type,
            int quantity,
            EventBus eventBus) {

        List<Entity> items = getItemsForInventory(engine, inventoryId);

        for (Entity e : items) {
            InventoryItemComponent item = Mappers.inventoryItem.get(e);
            if (item.type != type)
                continue;

            if (item.quantity > quantity) {
                setItemQuantity(e, item.quantity - quantity);
                eventBus.publish(new EntityUpdatedEvent(e));

                return true;
            } else {
                quantity -= item.quantity;
                item.quantity = 0;
                engine.removeEntity(e);
                eventBus.publish(new EntityRemovedEvent(e));
                if (quantity == 0)
                    return true;
            }
        }

        return quantity <= 0;
    }

    public static List<Entity> initializeInventory(
            Engine engine,
            InventoryInfo inventory,
            List<ItemStack> items,
            EventBus eventBus) {

        List<Entity> added = new ArrayList<>();
        Set<GridPoint2> occupied = new HashSet<>();

        float slotSize = getSlotSize();
        float itemSize = getItemSize();

        int x = 0;
        int y = 0;

        for (ItemStack stack : items) {
            boolean placed = false;

            for (; y < inventory.height; y++) {
                for (; x < inventory.width; x++) {
                    GridPoint2 pos = new GridPoint2(x, y);
                    if (!occupied.contains(pos)) {
                        occupied.add(pos);

                        InventoryItemComponent itemComponent = new InventoryItemComponent(
                                stack.type, stack.quantity, x, y, inventory.inventoryId);

                        Entity item = InventoryFactory.createInventoryItem(itemComponent, itemSize,
                                inventory.inventoryId);

                        if (inventory.inventoryType == InventoryType.HOTBAR) {
                            item.add(new PartOfHotbarComponent());
                        }

                        if (inventory.isOpen) {
                            float posX = inventory.startX + x * slotSize;
                            float posY = inventory.startY + (inventory.height - 1 - y) * slotSize;
                            Vector2 screenPos = applyItemSizeToSlotPosition(posX, posY);
                            item.add(new PositionComponent(screenPos.x, screenPos.y));
                        }

                        engine.addEntity(item);
                        eventBus.publish(new EntityAddedEvent(item));
                        added.add(item);
                        placed = true;
                        break;
                    }
                }
                x = 0;
                if (placed)
                    break;
            }

            if (!placed) {
                throw new IllegalStateException("Not enough space in inventory to initialize all items.");
            }
        }

        return added;
    }

    public static List<Entity> initializeTraderInventory(
            Engine engine,
            InventoryInfo inventory,
            List<ItemTrade> trades,
            EventBus bus) {

        List<ItemStack> itemStacks = new ArrayList<>();
        for (ItemTrade trade : trades) {
            itemStacks.add(new ItemStack(trade.type, 1));
        }

        List<Entity> addedItems = initializeInventory(engine, inventory, itemStacks, bus);

        for (int i = 0; i < addedItems.size(); i++) {
            Entity item = addedItems.get(i);
            ItemTrade trade = trades.get(i);

            item.add(new TradeableComponent(trade.price));
            item.remove(SyncComponent.class);

            TextComponent text = Mappers.text.get(item);
            if (text != null) {
                text.text = "$" + trade.price;
            }
        }

        return addedItems;
    }

    public static void enableInventoryItemDragging(Engine engine) {
        for (Entity entity : engine
                .getEntitiesFor(Family.all(InventoryItemComponent.class).exclude(TradeableComponent.class).get())) {
            if (!Mappers.draggable.has(entity)) {
                entity.add(new DraggableComponent());
            }
        }
    }

    public static void disableInventoryItemDragging(Engine engine) {
        for (Entity entity : engine
                .getEntitiesFor(Family.all(InventoryItemComponent.class, DraggableComponent.class).get())) {
            entity.remove(DraggableComponent.class);
        }
    }

    public static void addInventoryToggleButtonToEngine(Engine engine, InventoryInfo hotbar,
            InventoryInfo inventoryToToggle) {
        engine.addEntity(InventoryFactory.createInventoryToggleButton(hotbar, inventoryToToggle));
    }

    public static float getHotbarStartY() {
        return 20f;
    }

    public static float getSlotSize() {
        return TileUtils.getTileWidth() * 1.2f;
    }

    public static float getItemSize() {
        return getSlotSize() * 0.9f;
    }

    public static Vector2 applyItemSizeToSlotPosition(float posX, float posY) {
        float slotSize = getSlotSize();
        float itemSize = getItemSize();
        float margin = (slotSize - itemSize) / 2f;

        posX += margin;
        posY += margin;

        return new Vector2(posX, posY);
    }

    public static boolean isAnyInventoryOpen(Engine engine) {
        for (Entity entity : engine
                .getEntitiesFor(Family.all(InventoryComponent.class).get())) {
            InventoryComponent inventory = Mappers.inventory.get(entity);
            if (inventory.info.isOpen) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasStackOfType(Engine engine, String inventoryId, PrefabType type) {
        ImmutableArray<Entity> items = engine.getEntitiesFor(Family.all(InventoryItemComponent.class).get());

        for (Entity e : items) {
            InventoryItemComponent comp = Mappers.inventoryItem.get(e);
            if (comp.inventoryId.equals(inventoryId) && comp.type == type) {
                return true;
            }
        }

        return false;
    }

    public static Entity getSlotAtScreenPosition(Engine engine, int screenX, int screenY) {
        Vector2 mousePos = new Vector2(screenX, screenY);

        ImmutableArray<Entity> slotEntities = engine.getEntitiesFor(
                Family.all(InventorySlotComponent.class, PositionComponent.class, SizeComponent.class).get());

        for (Entity slot : slotEntities) {
            PositionComponent pos = Mappers.position.get(slot);
            SizeComponent size = Mappers.size.get(slot);

            boolean isInside = mousePos.x >= pos.position.x &&
                    mousePos.x <= pos.position.x + size.width &&
                    mousePos.y >= pos.position.y &&
                    mousePos.y <= pos.position.y + size.height;

            if (isInside) {
                return slot;
            }
        }

        return null;
    }

    public static void unselectSelectedHotbarItems(Engine engine) {
        for (Entity selectedHotbarEntity : engine
                .getEntitiesFor(Family.all(SelectedHotbarEntryComponent.class).get())) {
            selectedHotbarEntity.remove(SelectedHotbarEntryComponent.class);

            if (Mappers.inventorySlot.has(selectedHotbarEntity)) {
                SpriteComponent sprite = Mappers.sprite.get(selectedHotbarEntity);
                if (sprite != null) {
                    sprite.setTexturePath("inventory_slot.png");
                }
            }
        }
    }

    public static void setItemQuantity(Entity item, int newQuantity) {
        InventoryItemComponent itemComponent = Mappers.inventoryItem.get(item);
        TextComponent textComponent = Mappers.text.get(item);

        itemComponent.quantity = newQuantity;
        textComponent.text = String.valueOf(newQuantity);
    }

    public static boolean isHotbar(Engine engine, String inventoryId) {
        for (Entity e : engine.getEntitiesFor(Family.all(HotbarComponent.class).get())) {
            HotbarComponent hotbar = Mappers.hotbar.get(e);
            if (hotbar.info.inventoryId.equals(inventoryId)) {
                return hotbar.info.inventoryType == InventoryType.HOTBAR;
            }
        }
        return false;
    }
}
