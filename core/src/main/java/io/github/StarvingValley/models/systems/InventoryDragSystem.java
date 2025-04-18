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
import io.github.StarvingValley.models.components.PartOfHotbarComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.events.EntityUpdatedEvent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.utils.InventoryUtils;
import io.github.StarvingValley.utils.ScreenUtils;

public class InventoryDragSystem extends EntitySystem {
    private final GameContext context;

    public InventoryDragSystem(GameContext context) {
        this.context = context;
    }

    @Override
    public void update(float deltaTime) {
        ImmutableArray<Entity> draggedItems = getEngine().getEntitiesFor(
                Family.all(InventoryItemComponent.class, DraggingComponent.class, PositionComponent.class)
                        .exclude(DragEndComponent.class).get());

        if (!InventoryUtils.isAnyInventoryOpen(getEngine())) {
            for (Entity entity : draggedItems) {
                resetItemPosition(entity);
            }

            return;
        }

        Vector2 mousePos = ScreenUtils.getMouseScreenPosition();

        for (Entity entity : draggedItems) {
            PositionComponent position = Mappers.position.get(entity);
            SizeComponent size = Mappers.size.get(entity);
            position.position.set(mousePos.x - size.width / 2f, mousePos.y - size.height / 2f, position.position.z);
        }

        ImmutableArray<Entity> droppedItems = getEngine().getEntitiesFor(
                Family.all(InventoryItemComponent.class, DraggingComponent.class, DragEndComponent.class).get());

        for (Entity entity : droppedItems) {
            DragEndComponent dragEnd = Mappers.dragEnd.get(entity);
            boolean changedPos = placeItemInSlot(entity, dragEnd.dropScreenX, dragEnd.dropScreenY);

            entity.remove(DraggingComponent.class);
            entity.remove(DragEndComponent.class);

            if (changedPos) {
                context.eventBus.publish(new EntityUpdatedEvent(entity));
            }
        }
    }

    private boolean placeItemInSlot(Entity draggedItem, int screenX, int screenY) {
        Entity matchingSlot = InventoryUtils.getSlotAtScreenPosition(getEngine(), screenX, screenY);
        if (matchingSlot == null) {
            resetItemPosition(draggedItem);
            return false;
        }

        InventorySlotComponent slotData = Mappers.inventorySlot.get(matchingSlot);
        InventoryItemComponent draggedData = Mappers.inventoryItem.get(draggedItem);
        PositionComponent draggedPos = Mappers.position.get(draggedItem);

        Entity otherItem = InventoryUtils.getItemAt(getEngine(), slotData.inventoryId, slotData.slotX, slotData.slotY);

        int originX = draggedData.slotX;
        int originY = draggedData.slotY;
        String originId = draggedData.inventoryId;

        boolean originIsAHotbar = InventoryUtils.isHotbar(getEngine(), originId);
        boolean targetIsAHotbar = InventoryUtils.isHotbar(getEngine(), slotData.inventoryId);

        if (otherItem != null && !otherItem.equals(draggedItem)) {
            InventoryItemComponent otherData = Mappers.inventoryItem.get(otherItem);
            PositionComponent otherPos = Mappers.position.get(otherItem);

            otherData.slotX = originX;
            otherData.slotY = originY;
            otherData.inventoryId = originId;

            Entity originSlot = InventoryUtils.getSlot(getEngine(), originId, originX, originY);
            if (originSlot != null && Mappers.position.has(originSlot)) {
                Vector3 originPos = Mappers.position.get(originSlot).position;
                Vector2 originPosForItem = InventoryUtils.applyItemSizeToSlotPosition(originPos.x, originPos.y);
                otherPos.position.set(originPosForItem.x, originPosForItem.y, otherPos.position.z);
            }

            context.eventBus.publish(new EntityUpdatedEvent(otherItem));
        }

        draggedData.slotX = slotData.slotX;
        draggedData.slotY = slotData.slotY;
        draggedData.inventoryId = slotData.inventoryId;

        if (Mappers.position.has(matchingSlot)) {
            Vector3 slotPos = Mappers.position.get(matchingSlot).position;
            Vector2 itemPos = InventoryUtils.applyItemSizeToSlotPosition(slotPos.x, slotPos.y);
            draggedPos.position.set(itemPos.x, itemPos.y, draggedPos.position.z);
        }

        context.eventBus.publish(new EntityUpdatedEvent(draggedItem));

        if (originIsAHotbar != targetIsAHotbar) {
            updatePartOfHotbarComponent(draggedItem, slotData.inventoryId, targetIsAHotbar);
            if (otherItem != null) {
                updatePartOfHotbarComponent(otherItem, draggedData.inventoryId, originIsAHotbar);
            }
        }

        return true;
    }

    private void resetItemPosition(Entity item) {
        InventoryItemComponent itemData = Mappers.inventoryItem.get(item);
        PositionComponent pos = Mappers.position.get(item);

        Entity matchingSlot = InventoryUtils.getSlot(getEngine(), itemData.inventoryId, itemData.slotX, itemData.slotY);

        if (matchingSlot != null && Mappers.position.has(matchingSlot)) {
            Vector3 slotPos = Mappers.position.get(matchingSlot).position;
            Vector2 itemPos = InventoryUtils.applyItemSizeToSlotPosition(slotPos.x, slotPos.y);
            pos.position.set(itemPos.x, itemPos.y, pos.position.z);
        }
    }

    private void updatePartOfHotbarComponent(Entity item, String newInventoryId, boolean newInventoryIsHotbar) {
        boolean hasComponent = Mappers.partOfHotbar.has(item);

        if (newInventoryIsHotbar && !hasComponent) {
            item.add(new PartOfHotbarComponent());
        } else if (!newInventoryIsHotbar && hasComponent) {
            item.remove(PartOfHotbarComponent.class);
        }
    }
}
