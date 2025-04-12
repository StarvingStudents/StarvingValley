package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.DragEndComponent;
import io.github.StarvingValley.models.components.DraggingComponent;
import io.github.StarvingValley.models.components.InventoryItemComponent;
import io.github.StarvingValley.models.components.InventorySlotComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.Inventory;
import io.github.StarvingValley.models.types.InventorySlot;
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

        Inventory inventory = Mappers.inventory.get(player).inventory;

        ImmutableArray<Entity> draggedItems = getEngine().getEntitiesFor(
                Family.all(InventoryItemComponent.class, DraggingComponent.class, PositionComponent.class)
                        .exclude(DragEndComponent.class).get());

        Vector2 mousePos = ScreenUtils.getMouseScreenPosition();

        for (Entity entity : draggedItems) {
            PositionComponent position = Mappers.position.get(entity);
            SizeComponent size = Mappers.size.get(entity);

            float centeredX = mousePos.x - size.width / 2f;
            float centeredY = mousePos.y - size.height / 2f;

            position.position.set(centeredX, centeredY, position.position.z);
        }

        ImmutableArray<Entity> droppedItems = getEngine().getEntitiesFor(
                Family.all(InventoryItemComponent.class, DraggingComponent.class, DragEndComponent.class).get());

        for (Entity entity : droppedItems) {
            DragEndComponent dragEnd = Mappers.dragEnd.get(entity);
            DraggingComponent dragging = Mappers.dragging.get(entity);

            placeItemInSlot(entity, inventory, dragEnd.dropScreenX, dragEnd.dropScreenY, dragging.originScreenX,
                    dragging.originScreenY);
        }
    }

    private void placeItemInSlot(Entity draggedItem, Inventory inventory, int screenX, int screenY, int originX,
            int originY) {
        Entity matchingSlot = InventoryUtils.getSlotAtScreenPosition(getEngine(), screenX, screenY);
        Vector3 draggedPos = Mappers.position.get(draggedItem).position;
        InventoryItemComponent draggedItemData = Mappers.inventoryItem.get(draggedItem);

        if (matchingSlot == null) {
            resetItemPosition(draggedItemData, inventory, draggedPos);
            return;
        }

        InventorySlotComponent slotInfo = Mappers.inventorySlot.get(matchingSlot);
        int targetX = slotInfo.slotX;
        int targetY = slotInfo.slotY;

        Entity itemAlreadyInSlot = getItemAlreadyInSlot(draggedItem, targetX, targetY);
        if (itemAlreadyInSlot != null) {
            placeOtherItemOnOrigin(itemAlreadyInSlot, draggedItemData, inventory);
        }

        Vector2 targetSlotPixel = InventoryUtils.getPixelPositionForSlot(
                targetX, targetY, inventory.width, inventory.height);

        draggedPos.set(targetSlotPixel.x, targetSlotPixel.y, draggedPos.z);
        draggedItemData.inventorySlot.x = targetX;
        draggedItemData.inventorySlot.y = targetY;
    }

    private Entity getItemAlreadyInSlot(Entity draggedItem, int targetX, int targetY) {
        ImmutableArray<Entity> allItems = getEngine().getEntitiesFor(
                Family.all(InventoryItemComponent.class, PositionComponent.class).get());

        for (Entity other : allItems) {
            if (other == draggedItem)
                continue;

            InventoryItemComponent otherData = Mappers.inventoryItem.get(other);
            if (otherData.inventorySlot.x == targetX && otherData.inventorySlot.y == targetY) {
                return other;
            }
        }

        return null;
    }

    private void resetItemPosition(InventoryItemComponent draggedItemData, Inventory inventory,
            Vector3 draggedPos) {
        Vector2 originSlotPos = InventoryUtils.getPixelPositionForSlot(
                draggedItemData.inventorySlot.x,
                draggedItemData.inventorySlot.y,
                inventory.width,
                inventory.height);
        draggedPos.set(originSlotPos.x, originSlotPos.y, draggedPos.z);
    }

    private void placeOtherItemOnOrigin(Entity itemAlreadyInSlot, InventoryItemComponent draggedItemData,
            Inventory inventory) {
        InventorySlot otherItemData = Mappers.inventoryItem.get(itemAlreadyInSlot).inventorySlot;
        Vector3 otherItemPos = Mappers.position.get(itemAlreadyInSlot).position;

        Vector2 originSlotPixel = InventoryUtils.getPixelPositionForSlot(
                draggedItemData.inventorySlot.x, draggedItemData.inventorySlot.y,
                inventory.width, inventory.height);

        otherItemPos.set(originSlotPixel.x, originSlotPixel.y, otherItemPos.z);
        otherItemData.x = draggedItemData.inventorySlot.x;
        otherItemData.y = draggedItemData.inventorySlot.y;
    }
}
