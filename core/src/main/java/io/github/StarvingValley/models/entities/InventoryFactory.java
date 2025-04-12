package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;

import io.github.StarvingValley.models.components.DraggableComponent;
import io.github.StarvingValley.models.components.HudComponent;
import io.github.StarvingValley.models.components.InventoryItemComponent;
import io.github.StarvingValley.models.components.InventorySlotComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.TextComponent;
import io.github.StarvingValley.models.types.InventorySlot;

public class InventoryFactory {
    public static Entity createInventorySlot(float posX, float posY, int inventoryPosX, int inventoryPosY, float size) {
        Entity entity = new Entity();

        entity.add(new PositionComponent(posX, posY));
        entity.add(new SizeComponent(size, size));
        entity.add(new SpriteComponent("inventory_slot.png"));
        entity.add(new HudComponent());
        entity.add(new InventorySlotComponent(inventoryPosX, inventoryPosY));

        return entity;
    }

    public static Entity createInventoryItem(InventorySlot slot, float posX, float posY, float size) {
        Entity entity = new Entity();

        entity.add(new PositionComponent(posX, posY));
        entity.add(new SizeComponent(size, size));
        entity.add(new SpriteComponent(slot.getType().getIconName()));
        entity.add(new HudComponent());
        entity.add(new DraggableComponent());
        entity.add(new InventoryItemComponent(slot));
        entity.add(new TextComponent(String.valueOf(slot.getQuantity()), size * 3 / 4, size / 4));

        return entity;
    }
}
