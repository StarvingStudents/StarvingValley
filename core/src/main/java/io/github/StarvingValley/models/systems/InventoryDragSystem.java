package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.DragEndComponent;
import io.github.StarvingValley.models.components.DraggingComponent;
import io.github.StarvingValley.models.components.InventoryItemComponent;
import io.github.StarvingValley.models.components.InventorySlotComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.Inventory;
import io.github.StarvingValley.utils.InventoryUtils;
import io.github.StarvingValley.utils.ScreenUtils;

//TODO: Allow items to switch positions
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

    private void placeItemInSlot(Entity itemEntity, Inventory inventory, int screenX, int screenY, int originX,
            int originY) {

        Entity matchingSlot = InventoryUtils.getSlotAtScreenPosition(getEngine(), screenX, screenY);

        PositionComponent itemPos = Mappers.position.get(itemEntity);
        InventoryItemComponent item = Mappers.inventoryItem.get(itemEntity);

        if (matchingSlot != null) {
            PositionComponent targetPos = Mappers.position.get(matchingSlot);
            itemPos.position.set(targetPos.position.x, targetPos.position.y, itemPos.position.z);

            InventorySlotComponent slotInfo = Mappers.inventorySlot.get(matchingSlot);
            item.inventorySlot.x = slotInfo.slotX;
            item.inventorySlot.y = slotInfo.slotY;
        } else {
            Vector2 slotPos = InventoryUtils.getPixelPositionForSlot(
                    item.inventorySlot.x,
                    item.inventorySlot.y,
                    inventory.width, inventory.height);

            itemPos.position.set(slotPos.x, slotPos.y, itemPos.position.z);
        }
    }
}
