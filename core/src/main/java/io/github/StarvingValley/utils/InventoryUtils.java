package io.github.StarvingValley.utils;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.InventoryItemComponent;
import io.github.StarvingValley.models.components.InventorySlotComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.entities.InventoryFactory;
import io.github.StarvingValley.models.types.Inventory;
import io.github.StarvingValley.models.types.InventorySlot;

public class InventoryUtils {
    public static void openInventory(Engine engine, Inventory inventory) {
        // Should only ever be 1 inventory on screen
        closeInventory(engine);

        float slotSizePx = TileUtils.getTileWidth() * 1.2f;

        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        float totalInventoryWidth = inventory.width * slotSizePx;
        float totalInventoryHeight = inventory.height * slotSizePx;
        float startX = (screenWidth - totalInventoryWidth) / 2f;
        float startY = screenHeight / 2 - totalInventoryHeight / 2;

        for (int y = 0; y < inventory.height; y++) {
            for (int x = 0; x < inventory.width; x++) {
                float posX = startX + x * slotSizePx;
                float posY = startY + (inventory.height - 1 - y) * slotSizePx;

                Entity backgroundEntity = InventoryFactory.createInventorySlot(posX, posY, x, y, slotSizePx);
                engine.addEntity(backgroundEntity);

                InventorySlot slot = inventory.getSlotAt(x, y);
                if (slot != null && slot.getQuantity() > 0) {
                    Entity iconEntity = InventoryFactory.createInventoryItem(slot, posX, posY, slotSizePx);

                    engine.addEntity(iconEntity);
                }
            }
        }
    }

    public static void closeInventory(Engine engine) {
        engine.removeAllEntities(Family.all(InventorySlotComponent.class).get());
        engine.removeAllEntities(Family.all(InventoryItemComponent.class).get());
    }

    public static void toggleInventory(Engine engine, Inventory inventory) {
        if (isInventoryOpen(engine)) {
            closeInventory(engine);
        } else {
            InventoryUtils.openInventory(engine, inventory);
        }
    }

    public static boolean isInventoryOpen(Engine engine) {
        return engine.getEntitiesFor(
                Family.all(InventorySlotComponent.class).get()).size() > 0;
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
            int inventoryWidth,
            int inventoryHeight) {
        // TODO: Centralize this variable somehow
        float slotSizePx = TileUtils.getTileWidth() * 1.2f;

        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        float inventoryPixelWidth = inventoryWidth * slotSizePx;
        float inventoryPixelHeight = inventoryHeight * slotSizePx;

        float startX = (screenWidth - inventoryPixelWidth) / 2f;
        float startY = (screenHeight - inventoryPixelHeight) / 2f;

        int flippedY = inventoryHeight - 1 - slotY;

        float posX = startX + slotX * slotSizePx;
        float posY = startY + flippedY * slotSizePx;

        return new Vector2(posX, posY);
    }
}
