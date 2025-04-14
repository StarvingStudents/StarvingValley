package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.*;
import io.github.StarvingValley.models.events.EntityUpdatedEvent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.Inventory;
import io.github.StarvingValley.models.types.InventorySlot;
import io.github.StarvingValley.models.types.UiInventoryLayout;
import io.github.StarvingValley.utils.InventoryUtils;
import io.github.StarvingValley.utils.ScreenUtils;

public class InventoryDragSystem extends EntitySystem {
    private final GameContext context;

    public InventoryDragSystem(GameContext context) {
        this.context = context;
    }

    @Override
    public void update(float deltaTime) {
        Entity player = context.player;
        if (player == null || !Mappers.inventory.has(player) || !Mappers.hotbar.has(player)) {
            return;
        }

        ImmutableArray<Entity> draggedItems = getEngine().getEntitiesFor(
                Family.all(InventoryItemComponent.class, DraggingComponent.class, PositionComponent.class)
                        .exclude(DragEndComponent.class).get());

        Vector2 mousePos = ScreenUtils.getMouseScreenPosition();

        for (Entity entity : draggedItems) {
            PositionComponent position = Mappers.position.get(entity);
            SizeComponent size = Mappers.size.get(entity);
            position.position.set(mousePos.x - size.width / 2f, mousePos.y - size.height / 2f, position.position.z);
        }

        ImmutableArray<Entity> droppedItems = getEngine().getEntitiesFor(
                Family.all(InventoryItemComponent.class, DraggingComponent.class, DragEndComponent.class).get());

        boolean inventoryHasChanged = false;

        for (Entity entity : droppedItems) {
            DragEndComponent dragEnd = Mappers.dragEnd.get(entity);
            DraggingComponent dragging = Mappers.dragging.get(entity);

            inventoryHasChanged |= placeItemInSlot(entity, dragEnd.dropScreenX, dragEnd.dropScreenY,
                    dragging.originScreenX, dragging.originScreenY);

            entity.remove(DraggingComponent.class);
            entity.remove(DragEndComponent.class);
        }

        if (inventoryHasChanged) {
            context.eventBus.publish(new EntityUpdatedEvent(player));
        }
    }

    private boolean placeItemInSlot(Entity draggedItem, int screenX, int screenY, int originScreenX,
            int originScreenY) {
        Entity matchingSlot = InventoryUtils.getSlotAtScreenPosition(getEngine(), screenX, screenY);
        InventoryItemComponent draggedItemData = Mappers.inventoryItem.get(draggedItem);
        Vector3 draggedPos = Mappers.position.get(draggedItem).position;

        boolean originIsHotbar = Mappers.hotbarUi.has(draggedItem);
        Inventory originInventory = originIsHotbar
                ? Mappers.hotbar.get(context.player).hotbar
                : Mappers.inventory.get(context.player).inventory;

        if (matchingSlot == null) {
            resetItemPosition(draggedItem, originInventory, originIsHotbar);
            return false;
        }

        boolean targetIsHotbar = Mappers.hotbarUi.has(matchingSlot);
        Inventory targetInventory = targetIsHotbar
                ? Mappers.hotbar.get(context.player).hotbar
                : Mappers.inventory.get(context.player).inventory;

        InventorySlotComponent targetSlotInfo = Mappers.inventorySlot.get(matchingSlot);

        int originSlotX = draggedItemData.slotX;
        int originSlotY = draggedItemData.slotY;

        Entity itemAlreadyInSlot = getItemAlreadyInSlot(targetSlotInfo.slotX, targetSlotInfo.slotY, targetIsHotbar);

        originInventory.removeSlotAt(originSlotX, originSlotY);

        if (itemAlreadyInSlot != null) {
            InventoryItemComponent otherItemData = Mappers.inventoryItem.get(itemAlreadyInSlot);
            Vector3 otherPos = Mappers.position.get(itemAlreadyInSlot).position;

            otherItemData.slotX = originSlotX;
            otherItemData.slotY = originSlotY;

            UiInventoryLayout originLayout = originIsHotbar
                    ? InventoryUtils.getHotbarLayout(originInventory)
                    : InventoryUtils.getInventoryLayout(originInventory);

            Vector2 originPixel = InventoryUtils.getPixelPositionForSlot(originSlotX, originSlotY, originLayout);
            otherPos.set(originPixel.x, originPixel.y, otherPos.z);

            targetInventory.removeSlotAt(targetSlotInfo.slotX, targetSlotInfo.slotY);
            originInventory.addOrReplaceSlot(
                    new InventorySlot(otherItemData.type, otherItemData.quantity, originSlotX, originSlotY));

            if (originIsHotbar && !Mappers.hotbarUi.has(itemAlreadyInSlot)) {
                itemAlreadyInSlot.remove(InventoryUiComponent.class);
                itemAlreadyInSlot.add(new HotbarUiComponent());
            } else if (!originIsHotbar && !Mappers.inventoryUi.has(itemAlreadyInSlot)) {
                itemAlreadyInSlot.remove(HotbarUiComponent.class);
                itemAlreadyInSlot.add(new InventoryUiComponent());
            }
        }

        draggedItemData.slotX = targetSlotInfo.slotX;
        draggedItemData.slotY = targetSlotInfo.slotY;

        UiInventoryLayout targetLayout = targetIsHotbar
                ? InventoryUtils.getHotbarLayout(targetInventory)
                : InventoryUtils.getInventoryLayout(targetInventory);

        Vector2 targetPixelPos = InventoryUtils.getPixelPositionForSlot(targetSlotInfo.slotX, targetSlotInfo.slotY,
                targetLayout);
        draggedPos.set(targetPixelPos.x, targetPixelPos.y, draggedPos.z);

        targetInventory.addOrReplaceSlot(new InventorySlot(draggedItemData.type, draggedItemData.quantity,
                draggedItemData.slotX, draggedItemData.slotY));

        if (originIsHotbar != targetIsHotbar) {
            if (originIsHotbar) {
                draggedItem.remove(HotbarUiComponent.class);
                draggedItem.add(new InventoryUiComponent());
            } else {
                draggedItem.remove(InventoryUiComponent.class);
                draggedItem.add(new HotbarUiComponent());
            }
        }

        return true;
    }

    private Entity getItemAlreadyInSlot(int x, int y, boolean isHotbar) {
        ImmutableArray<Entity> items = getEngine().getEntitiesFor(
                Family.all(InventoryItemComponent.class, PositionComponent.class).get());

        for (Entity item : items) {
            if (Mappers.hotbarUi.has(item) != isHotbar)
                continue;

            InventoryItemComponent data = Mappers.inventoryItem.get(item);
            if (data.slotX == x && data.slotY == y)
                return item;
        }

        return null;
    }

    private void resetItemPosition(Entity item, Inventory inventory, boolean isHotbar) {
        InventoryItemComponent data = Mappers.inventoryItem.get(item);
        Vector3 pos = Mappers.position.get(item).position;

        UiInventoryLayout layout = isHotbar
                ? InventoryUtils.getHotbarLayout(inventory)
                : InventoryUtils.getInventoryLayout(inventory);

        Vector2 originalPos = InventoryUtils.getPixelPositionForSlot(data.slotX, data.slotY, layout);
        pos.set(originalPos.x, originalPos.y, pos.z);
    }
}
