package io.github.StarvingValley.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.controllers.InventoryController;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.Inventory;
import io.github.StarvingValley.models.types.InventorySlot;
import io.github.StarvingValley.utils.TextureUtils;

public class InventoryView {
    private final InventoryController controller;
    private final BitmapFont font;
    private final GameContext context;

    private Inventory inventory;

    private InventorySlot draggingSlot;
    private Inventory draggingFromInventory;

    private boolean dragJustCompleted = false;

    private Texture slotBackgroundTexture;

    public InventoryView(InventoryController controller, GameContext context) {
        this.controller = controller;
        this.context = context;

        this.font = new BitmapFont();
        this.font.getData().setScale(4f);

        this.slotBackgroundTexture = context.assets.getTexture("inventory_slot.png");
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void update() {
        if (inventory == null || !controller.isInventoryVisible()) {
            draggingSlot = null;
            draggingFromInventory = null;
            return;
        }

        Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

        if (!Gdx.input.isTouched()) {
            if (draggingSlot != null && !dragJustCompleted) {
                controller.onDragCompleted(draggingSlot, draggingFromInventory, mouse);
                dragJustCompleted = true;

                draggingSlot = null;
                draggingFromInventory = null;
            }
        } else {
            dragJustCompleted = false;
        }

        if (draggingSlot == null && Gdx.input.justTouched() && !dragJustCompleted) {
            int tileWidth = Gdx.graphics.getWidth() / Config.CAMERA_TILES_WIDE;

            Inventory hotbar = getHotbarIfVisible();
            Inventory[] inventories = { inventory, hotbar };

            for (Inventory inv : inventories) {
                if (inv == null)
                    continue;

                Vector2 origin = getInventoryPosition(inv);
                GridPoint2 slotPos = controller.screenToSlot(mouse, origin, inv, tileWidth);

                if (slotPos != null) {
                    InventorySlot slot = inv.getSlotAt((int) slotPos.x, (int) slotPos.y);
                    if (slot != null) {
                        draggingSlot = slot;
                        draggingFromInventory = inv;
                        break;
                    }
                }
            }
        }
    }

    public void render(SpriteBatch batch) {
        int tileWidth = Gdx.graphics.getWidth() / Config.CAMERA_TILES_WIDE;

        if (inventory != null && controller.isInventoryVisible()) {
            drawInventory(batch, inventory, tileWidth);
        }

        Inventory hotbar = getHotbarIfVisible();
        if (hotbar != null) {
            drawInventory(batch, hotbar, tileWidth);
        }

        if (draggingSlot != null) {
            Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            Sprite sprite = TextureUtils.getSpriteForPrefabType(draggingSlot.itemStack.type, context.assets);
            if (sprite != null) {
                sprite.setSize(tileWidth, tileWidth);
                sprite.setPosition(mouse.x - tileWidth / 2f, mouse.y - tileWidth / 2f);
                sprite.draw(batch);

                String text = String.valueOf(draggingSlot.itemStack.quantity);
                font.draw(batch, text, sprite.getX() + tileWidth * 2 / 3f, sprite.getY() + tileWidth / 4f);
            }
        }
    }

    private void drawInventory(SpriteBatch batch, Inventory inv, int tileWidth) {
        Vector2 origin = getInventoryPosition(inv);

        for (int y = 0; y < inv.height; y++) {
            for (int x = 0; x < inv.width; x++) {
                float slotX = origin.x + x * tileWidth;
                float slotY = origin.y - y * tileWidth;

                boolean hide = draggingSlot != null
                        && draggingFromInventory == inv
                        && draggingSlot.x == x
                        && draggingSlot.y == y;

                if (hide) {
                    drawSlotBackground(batch, slotX, slotY, tileWidth);
                } else {
                    InventorySlot slot = inv.getSlotAt(x, y);
                    drawInventorySlot(batch, slot, slotX, slotY, tileWidth);
                }
            }
        }
    }

    private void drawInventorySlot(SpriteBatch batch, InventorySlot slot, float x, float y, int size) {
        drawSlotBackground(batch, x, y, size);

        if (slot == null)
            return;

        Sprite sprite = TextureUtils.getSpriteForPrefabType(slot.itemStack.type, context.assets);
        if (sprite == null)
            return;

        float iconSize = size * 0.8f;
        float offset = (size - iconSize) / 2f;

        sprite.setSize(iconSize, iconSize);
        sprite.setPosition(x + offset, y - size + offset);
        sprite.draw(batch);

        String text = String.valueOf(slot.itemStack.quantity);
        float textX = x + size * 2 / 3f - font.getRegion().getRegionWidth() / 10f;
        float textY = y - size + size / 4f;
        font.draw(batch, text, textX, textY);
    }

    private void drawSlotBackground(SpriteBatch batch, float x, float y, int size) {
        if (slotBackgroundTexture != null) {
            Sprite bg = new Sprite(slotBackgroundTexture);
            bg.setSize(size, size);
            bg.setPosition(x, y - size);
            bg.draw(batch);
        }
    }

    private Vector2 getInventoryPosition(Inventory inv) {
        return inv == inventory
                ? controller.calculateInventoryPosition(inv)
                : controller.calculateHotbarPosition(inv);
    }

    private Inventory getHotbarIfVisible() {
        if (controller.isHotbarVisible()
                && context.player != null
                && Mappers.hotbar.has(context.player)) {
            return Mappers.hotbar.get(context.player).hotbar;
        }
        return null;
    }
}
