package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;

import io.github.StarvingValley.models.components.ClickableComponent;
import io.github.StarvingValley.models.components.DraggableComponent;
import io.github.StarvingValley.models.components.HotbarUiComponent;
import io.github.StarvingValley.models.components.HudComponent;
import io.github.StarvingValley.models.components.InventoryItemComponent;
import io.github.StarvingValley.models.components.InventorySlotComponent;
import io.github.StarvingValley.models.components.InventoryToggleButtonComponent;
import io.github.StarvingValley.models.components.InventoryUiComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.TextComponent;
import io.github.StarvingValley.models.types.Inventory;
import io.github.StarvingValley.models.types.InventorySlot;
import io.github.StarvingValley.models.types.UiInventoryLayout;
import io.github.StarvingValley.utils.InventoryUtils;

public class InventoryFactory {
    public static Entity createInventorySlot(float posX, float posY, int inventoryPosX, int inventoryPosY, float size) {
        Entity entity = new Entity();

        entity.add(new PositionComponent(posX, posY));
        entity.add(new SizeComponent(size, size));
        entity.add(new SpriteComponent("inventory_slot.png"));
        entity.add(new HudComponent());
        entity.add(new InventoryUiComponent());
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
        entity.add(new InventoryItemComponent(slot.getType(), slot.getQuantity(), slot.x, slot.y));
        entity.add(new InventoryUiComponent());
        entity.add(new TextComponent(String.valueOf(slot.getQuantity()), size * 3 / 4, size / 4));

        return entity;
    }

    public static Entity createHotbarSlot(float posX, float posY, int hotbarPosX, int hotbarPosY, float size) {
        Entity entity = new Entity();

        entity.add(new PositionComponent(posX, posY));
        entity.add(new SizeComponent(size, size));
        entity.add(new SpriteComponent("inventory_slot.png"));
        entity.add(new HudComponent());
        entity.add(new HotbarUiComponent());
        entity.add(new InventorySlotComponent(hotbarPosX, hotbarPosY));

        return entity;
    }

    public static Entity createHotbarItem(InventorySlot slot, float posX, float posY, float size) {
        Entity entity = new Entity();

        entity.add(new PositionComponent(posX, posY));
        entity.add(new SizeComponent(size, size));
        entity.add(new SpriteComponent(slot.getType().getIconName()));
        entity.add(new HudComponent());
        entity.add(new DraggableComponent());
        entity.add(new ClickableComponent());
        entity.add(new InventoryItemComponent(slot.getType(), slot.getQuantity(), slot.x, slot.y));
        entity.add(new HotbarUiComponent());
        entity.add(new TextComponent(String.valueOf(slot.getQuantity()), size * 3 / 4, size / 4));

        return entity;
    }

    public static Entity createInventoryToggleButton(Inventory hotbar) {
        UiInventoryLayout hotbarLayout = InventoryUtils.getHotbarLayout(hotbar);
        Entity entity = new Entity();
        entity
                .add(new SpriteComponent("inventory_open_button.png"))
                .add(new PositionComponent(
                        hotbarLayout.startX + hotbarLayout.width * hotbarLayout.slotSizePx
                                + (hotbarLayout.slotSizePx / 4),
                        hotbarLayout.startY))
                .add(new HudComponent())
                .add(new ClickableComponent())
                .add(new InventoryToggleButtonComponent())
                .add(new SizeComponent(hotbarLayout.slotSizePx, hotbarLayout.slotSizePx));

        return entity;
    }
}
