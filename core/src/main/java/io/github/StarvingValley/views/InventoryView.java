package io.github.StarvingValley.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.controllers.InventoryController;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.InventoryComponent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.Inventory;
import io.github.StarvingValley.models.types.InventorySlot;
import io.github.StarvingValley.utils.TextureUtils;

//TODO: Check if its cleaner if inventory slots etc are ecs entities
public class InventoryView {
    private final InventoryController controller;
    private final BitmapFont font;
    private final GameContext context;

    private Inventory inventory;

    private InventorySlot draggingSlot;
    private Inventory draggingFromInventory;

    private boolean dragJustCompleted = false;

    private Texture slotBackgroundTexture;
    private Texture slotBackgroundHighlightedTexture;

    private Texture buttonTexture;
    private Rectangle buttonBounds;

    public InventoryView(InventoryController controller, GameContext context) {
        this.controller = controller;
        this.context = context;

        this.font = new BitmapFont();
        this.font.getData().setScale(4f);

        this.slotBackgroundTexture = context.assets.getTexture("inventory_slot.png");
        this.slotBackgroundHighlightedTexture = context.assets.getTexture("inventory_slot_highlight.png");
        this.buttonTexture = context.assets.getTexture("inventory_open_button.png");
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void update() {
        Inventory hotbar = getHotbarIfVisible();

        Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

        // TODO: When selecting an item in inventory you also place
        if (Gdx.input.justTouched()) {
            if (hotbar != null) {
                Vector2 origin = getInventoryPosition(hotbar);
                GridPoint2 slot = controller.screenToSlot(mouse, origin, hotbar, controller.getSlotSize());

                if (slot != null && slot.y == 0 && controller.isInsideInventory(mouse, hotbar, origin)) {
                    if (controller.getSelectedHotbarIndex() == slot.x) {
                        controller.setSelectedHotbarIndex(-1);
                    } else {
                        controller.setSelectedHotbarIndex(slot.x);
                    }
                }
            }
        }

        if (hotbar != null) {
            Vector2 hotbarOrigin = controller.calculateHotbarPosition(hotbar);
            float size = controller.getSlotSize();
            float x = hotbarOrigin.x + hotbar.width * size + 10;
            float y = hotbarOrigin.y - size;
            buttonBounds = new Rectangle(x, y, size, size);

            if (Gdx.input.justTouched()) {
                if (buttonBounds != null && buttonBounds.contains(mouse)) {
                    if (controller.isInventoryIsVisible()) {
                        controller.setInventoryVisible(false);
                    } else {
                        InventoryComponent inventoryComponent = Mappers.inventory.get(context.player);
                        if (inventoryComponent != null) {
                            controller.setInventory(inventoryComponent.inventory);
                            controller.setInventoryVisible(true);
                        }
                    }
                }
            }
        }

        if (inventory == null || !controller.isInventoryVisible()) {
            draggingSlot = null;
            draggingFromInventory = null;
            return;
        }

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
            Inventory[] inventories = { inventory, hotbar };

            for (Inventory inv : inventories) {
                if (inv == null)
                    continue;

                Vector2 origin = getInventoryPosition(inv);
                GridPoint2 slotPos = controller.screenToSlot(mouse, origin, inv, controller.getSlotSize());

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
        if (inventory != null && controller.isInventoryVisible()) {
            drawInventory(batch, inventory, controller.getSlotSize());
        }

        Inventory hotbar = getHotbarIfVisible();
        if (hotbar != null) {
            drawInventory(batch, hotbar, controller.getSlotSize());
        }

        if (draggingSlot != null) {
            Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            Sprite sprite = TextureUtils.getSpriteForPrefabType(draggingSlot.itemStack.type, context.assets);
            if (sprite != null) {
                sprite.setSize(controller.getSlotSize(), controller.getSlotSize());
                sprite.setPosition(mouse.x - controller.getSlotSize() / 2f, mouse.y - controller.getSlotSize() / 2f);
                sprite.draw(batch);

                String text = String.valueOf(draggingSlot.itemStack.quantity);
                font.draw(batch, text,
                        sprite.getX() + controller.getSlotSize() * 2 / 3f,
                        sprite.getY() + controller.getSlotSize() / 4f);
            }
        }

        if (buttonTexture != null && buttonBounds != null && hotbar != null) {
            Sprite sprite = new Sprite(buttonTexture);
            sprite.setSize(buttonBounds.width, buttonBounds.height);
            sprite.setPosition(buttonBounds.x, buttonBounds.y);
            sprite.draw(batch);
        }
    }

    private void drawInventory(SpriteBatch batch, Inventory inv, int slotSize) {
        Vector2 origin = getInventoryPosition(inv);

        for (int y = 0; y < inv.height; y++) {
            for (int x = 0; x < inv.width; x++) {
                float slotX = origin.x + x * slotSize;
                float slotY = origin.y - y * slotSize;

                boolean hide = draggingSlot != null &&
                        draggingFromInventory == inv &&
                        draggingSlot.x == x &&
                        draggingSlot.y == y;

                InventorySlot slot = inv.getSlotAt(x, y);
                drawInventorySlot(batch, slot, inv, slotX, slotY, slotSize, !hide, x, y);
            }
        }
    }

    private void drawInventorySlot(SpriteBatch batch, InventorySlot slot, Inventory inv, float x, float y, int size,
            boolean showIcon, int inventorySlotX, int inventorySlotY) {
        boolean isSelectedHotbarSlot = inv == getHotbarIfVisible()
                && controller.getSelectedHotbarIndex() == inventorySlotX
                && inventorySlotY == 0;

        if (isSelectedHotbarSlot) {
            drawHighlight(batch, x, y, size);
        } else {
            drawSlotBackground(batch, x, y, size);
        }

        if (slot == null || !showIcon)
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

    private void drawHighlight(SpriteBatch batch, float x, float y, int size) {
        if (slotBackgroundHighlightedTexture != null) {
            Sprite bg = new Sprite(slotBackgroundHighlightedTexture);
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
