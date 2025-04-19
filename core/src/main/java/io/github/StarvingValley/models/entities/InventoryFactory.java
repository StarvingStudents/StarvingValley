package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;

import io.github.StarvingValley.models.components.ClickableComponent;
import io.github.StarvingValley.models.components.DraggableComponent;
import io.github.StarvingValley.models.components.HudComponent;
import io.github.StarvingValley.models.components.InventoryItemComponent;
import io.github.StarvingValley.models.components.InventorySlotComponent;
import io.github.StarvingValley.models.components.InventoryToggleButtonComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.SyncComponent;
import io.github.StarvingValley.models.components.TextComponent;
import io.github.StarvingValley.models.types.InventoryInfo;
import io.github.StarvingValley.utils.InventoryUtils;

public class InventoryFactory {
    public static Entity createInventorySlot(float posX, float posY, int inventoryPosX, int inventoryPosY, float size,
            String inventoryId) {
        Entity entity = new Entity();

        entity.add(new PositionComponent(posX, posY));
        entity.add(new SizeComponent(size, size));
        entity.add(new SpriteComponent("inventory_slot.png"));
        entity.add(new HudComponent());
        entity.add(new InventorySlotComponent(inventoryPosX, inventoryPosY, inventoryId));

        return entity;
    }

    public static Entity createInventoryItem(
            InventoryItemComponent item, float size, String inventoryId) {
        Entity entity = new Entity();

        entity.add(new SizeComponent(size, size));
        entity.add(new SpriteComponent(item.type.getIconName()));
        entity.add(new HudComponent());
        entity.add(new SyncComponent());
        entity.add(new DraggableComponent());
        entity.add(new ClickableComponent());
        entity.add(new InventoryItemComponent(item.type, item.quantity, item.slotX, item.slotY, inventoryId));
        entity.add(new TextComponent(String.valueOf(item.quantity), size * 3 / 4, size / 4));

        return entity;
    }

    public static Entity createInventoryToggleButton(InventoryInfo hotbar, InventoryInfo inventoryToToggle) {
        float slotSize = InventoryUtils.getSlotSize();

        int screenWidth = Gdx.graphics.getWidth();

        float totalInventoryWidth = hotbar.width * slotSize;
        float startX = (screenWidth - totalInventoryWidth) / 2f;
        float y = InventoryUtils.getHotbarStartY();

        Entity entity = new Entity();
        entity
                .add(new SpriteComponent("inventory_open_button.png"))
                .add(new PositionComponent(
                        startX + totalInventoryWidth
                                + (slotSize / 4),
                        y))
                .add(new HudComponent())
                .add(new ClickableComponent())
                .add(new InventoryToggleButtonComponent(inventoryToToggle))
                .add(new SizeComponent(slotSize, slotSize));

        return entity;
    }
}
