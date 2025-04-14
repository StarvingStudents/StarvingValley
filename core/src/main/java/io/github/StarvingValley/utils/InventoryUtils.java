package io.github.StarvingValley.utils;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.HotbarUiComponent;
import io.github.StarvingValley.models.components.InventorySlotComponent;
import io.github.StarvingValley.models.components.InventoryUiComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.entities.InventoryFactory;
import io.github.StarvingValley.models.types.Inventory;
import io.github.StarvingValley.models.types.InventorySlot;
import io.github.StarvingValley.models.types.UiInventoryLayout;

public class InventoryUtils {
    public static void addInventoryToEngine(Engine engine, Inventory inventory) {
        UiInventoryLayout layout = getInventoryLayout(inventory);

        for (int y = 0; y < inventory.height; y++) {
            for (int x = 0; x < inventory.width; x++) {
                float posX = layout.startX + x * layout.slotSizePx;
                float posY = layout.startY + (inventory.height - 1 - y) * layout.slotSizePx;

                Entity backgroundEntity = InventoryFactory.createInventorySlot(posX, posY, x, y,
                        layout.slotSizePx);
                engine.addEntity(backgroundEntity);

                InventorySlot slot = inventory.getSlotAt(x, y);
                if (slot != null && slot.getQuantity() > 0) {
                    Entity iconEntity = InventoryFactory.createInventoryItem(slot, posX, posY, layout.slotSizePx);

                    engine.addEntity(iconEntity);
                }
            }
        }
    }

    public static void addHotbarToEngine(Engine engine, Inventory hotbar) {
        UiInventoryLayout layout = getHotbarLayout(hotbar);

        for (int y = 0; y < hotbar.height; y++) {
            for (int x = 0; x < hotbar.width; x++) {
                float posX = layout.startX + x * layout.slotSizePx;
                float posY = layout.startY + (hotbar.height - 1 - y) * layout.slotSizePx;

                Entity backgroundEntity = InventoryFactory.createHotbarSlot(posX, posY, x, y,
                        layout.slotSizePx);
                engine.addEntity(backgroundEntity);

                InventorySlot slot = hotbar.getSlotAt(x, y);
                if (slot != null && slot.getQuantity() > 0) {
                    Entity iconEntity = InventoryFactory.createHotbarItem(slot, posX, posY, layout.slotSizePx);

                    engine.addEntity(iconEntity);
                }
            }
        }
    }

    public static void addInventoryToggleButtonToEngine(Engine engine, Inventory hotbar) {
      engine.addEntity(InventoryFactory.createInventoryToggleButton(hotbar));
    }

    public static UiInventoryLayout getInventoryLayout(Inventory inventory) {
        float slotSize = getSlotSize();

        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        float totalInventoryWidth = inventory.width * slotSize;
        float totalInventoryHeight = inventory.height * slotSize;
        float startX = (screenWidth - totalInventoryWidth) / 2f;
        float startY = screenHeight / 2 - totalInventoryHeight / 2 + (slotSize / 4);

        return new UiInventoryLayout(startX, startY, slotSize, inventory.width, inventory.height);
    }

    public static UiInventoryLayout getHotbarLayout(Inventory hotbar) {
        float slotSize = getSlotSize();
        float screenWidth = Gdx.graphics.getWidth();

        float hotbarWidthPx = hotbar.width * slotSize;
        float startX = (screenWidth - hotbarWidthPx) / 2f;
        float startY = 20f;

        return new UiInventoryLayout(startX, startY, slotSize, hotbar.width, hotbar.height);
    }

    public static float getSlotSize() {
        return TileUtils.getTileWidth() * 1.2f;
    }

    public static void closeInventory(Engine engine) {
        engine.removeAllEntities(Family.all(InventoryUiComponent.class).get());
    }

    public static void closeHotbar(Engine engine) {
        engine.removeAllEntities(Family.all(HotbarUiComponent.class).get());
    }

    public static void toggleInventory(Engine engine, Inventory inventory) {
        if (isInventoryOpen(engine)) {
            closeInventory(engine);
        } else {
            InventoryUtils.addInventoryToEngine(engine, inventory);
        }
    }

    public static void toggleHotbar(Engine engine, Inventory hotbar) {
        if (isHotbarOpen(engine)) {
            closeHotbar(engine);
        } else {
            InventoryUtils.addHotbarToEngine(engine, hotbar);
        }
    }

    public static boolean isInventoryOpen(Engine engine) {
        return engine.getEntitiesFor(
                Family.all(InventoryUiComponent.class).get()).size() > 0;
    }

    public static boolean isHotbarOpen(Engine engine) {
        return engine.getEntitiesFor(
                Family.all(HotbarUiComponent.class).get()).size() > 0;
    }

    // TODO: Make refreshInventory(Engine engine, Inventory inventory) that doesn't
    // remove and add entities but just update existing ones to match given
    // inventory

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

    public static Vector2 getPixelPositionForSlot(
            int slotX,
            int slotY,
            UiInventoryLayout layout) {
        int flippedY = layout.height - 1 - slotY;

        float posX = layout.startX + slotX * layout.slotSizePx;
        float posY = layout.startY + flippedY * layout.slotSizePx;

        return new Vector2(posX, posY);
    }
}
