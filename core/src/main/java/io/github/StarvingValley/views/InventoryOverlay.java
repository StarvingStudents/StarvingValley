package io.github.StarvingValley.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.StarvingValley.models.systems.InventorySystem;
import com.badlogic.ashley.core.Entity;

public class InventoryOverlay {
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private BitmapFont font;
    private Viewport viewport;
    private List<String> inventory;
    private List<Rectangle> slots;
    private Texture backgroundTexture;


    private static final int SLOT_SIZE = 60;
    private static final int SLOT_PADDING = 10;
    private static final int NUM_COLS = 7;
    private static final int NUM_ROWS = 5;
    private static final int NUM_SLOTS = NUM_COLS * NUM_ROWS;

    private static final int INVENTORY_WIDTH = NUM_COLS * (SLOT_SIZE + SLOT_PADDING) + SLOT_PADDING;
    private static final int INVENTORY_HEIGHT = NUM_ROWS * (SLOT_SIZE + SLOT_PADDING) + SLOT_PADDING;
    //private static final int INVENTORY_X = 50; // Adjust X position
    //private static final int INVENTORY_Y = 100; // Adjust Y position
    private static final int INVENTORY_MARGIN = 20;

    private boolean isDragging = false;
    private int draggedItemIndex = -1;
    //private float draggedItemStartX, draggedItemStartY;

    private String getItemDisplayName(String itemId) {
        if (itemId == null) return "";
        switch (itemId) {
            case "coin_id": return "5 coins";
            case "sword_id": return "sword";
            case "crop_id": return "3 crops";
            default: return "Unknown Item";
        }
    }

    public InventoryOverlay(Viewport viewport) {
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();
        this.viewport = viewport;

        // Initialize the inventory with some starting items (using IDs)
        inventory = new ArrayList<>();
        inventory.add("coin_id");
        inventory.add("sword_id");
        inventory.add("crop_id");

        // Pad the inventory with nulls to match the number of slots
        while (inventory.size() < NUM_SLOTS) {
            inventory.add(null);
        }

        // Calculate slot positions
        slots = new ArrayList<>();
        //float startX = INVENTORY_X + SLOT_PADDING;
        //float startY = INVENTORY_Y + INVENTORY_HEIGHT - SLOT_SIZE - SLOT_PADDING;
        float startX = viewport.getWorldWidth() - INVENTORY_WIDTH - INVENTORY_MARGIN + SLOT_PADDING;
        float startY = INVENTORY_MARGIN + SLOT_PADDING;
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                float slotX = startX + col * (SLOT_SIZE + SLOT_PADDING);
                float slotY = startY + (NUM_ROWS - 1 - row) * (SLOT_SIZE + SLOT_PADDING);
                slots.add(new Rectangle(slotX, slotY, SLOT_SIZE, SLOT_SIZE));
            }
        }
    }

    public void render(InventorySystem inventorySystem, Entity playerEntity) {
        viewport.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        batch.setProjectionMatrix(viewport.getCamera().combined);

        float inventoryX = viewport.getWorldWidth() - INVENTORY_WIDTH - INVENTORY_MARGIN;
        float inventoryY = INVENTORY_MARGIN;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0.7f, 0.5f, 0.3f, 1f));
        //shapeRenderer.rect(INVENTORY_X, INVENTORY_Y, INVENTORY_WIDTH, INVENTORY_HEIGHT);        shapeRenderer.end();
        shapeRenderer.rect(inventoryX, inventoryY, INVENTORY_WIDTH, INVENTORY_HEIGHT);
        shapeRenderer.end();

        // Draw slots
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        for (Rectangle slot : slots) {
            shapeRenderer.rect(slot.x, slot.y, slot.width, slot.height);
        }
        shapeRenderer.end();

        // Handle input
        handleInput(inventorySystem, playerEntity);

        // Draw inventory items in slots
        batch.begin();
        font.setColor(Color.BLACK);
        for (int i = 0; i < slots.size(); i++) {
            Rectangle slot = slots.get(i);
            String itemId = inventory.get(i);
            String displayName = getItemDisplayName(itemId); // Get display name
            if (!displayName.isEmpty()) {
                if (isDragging && i == draggedItemIndex) {
                    // Draw dragged item at mouse position
                    Vector2 mousePos = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
                    font.draw(batch, displayName, mousePos.x - SLOT_SIZE / 2 + SLOT_PADDING / 2, mousePos.y + SLOT_SIZE / 2 - SLOT_PADDING / 2);
                } else {
                    // Draw item in slot
                    font.draw(batch, displayName, slot.x + SLOT_PADDING / 2, slot.y + SLOT_SIZE - SLOT_PADDING / 2);
                }
            }
        }
        batch.end();
    }

    private void handleInput(InventorySystem inventorySystem, Entity playerEntity) {
        Vector2 mousePos = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (!isDragging) {
                // Check for drag start
                for (int i = 0; i < slots.size(); i++) {
                    if (slots.get(i).contains(mousePos) && inventory.get(i) != null) {
                        isDragging = true;
                        draggedItemIndex = i;
                        //draggedItemStartX = slots.get(i).x;
                        //draggedItemStartY = slots.get(i).y;
                        break;
                    }
                }
            }
        } else if (isDragging) {
            // Check for drag end
            isDragging = false;
            int destinationSlot = -1;
            for (int i = 0; i < slots.size(); i++) {
                if (slots.get(i).contains(mousePos)) {
                    destinationSlot = i;
                    break;
                }
            }

            if (destinationSlot != -1 && destinationSlot != draggedItemIndex) {
                String draggedItemId = inventory.get(draggedItemIndex);
                String destinationItemId = inventory.get(destinationSlot);

                // "Move" item by removing from old slot and adding to new
                if (draggedItemId != null) {
                    inventorySystem.removeItem(playerEntity, draggedItemId);
                }
                if (destinationItemId != null) {
                    inventorySystem.addItem(playerEntity, destinationItemId);
                }
                inventory.set(draggedItemIndex, destinationItemId);
                inventory.set(destinationSlot, draggedItemId);
            }
            draggedItemIndex = -1;
        }

        // Handle right-click removal
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            for (int i = 0; i < slots.size(); i++) {
                if (slots.get(i).contains(mousePos) && inventory.get(i) != null) {
                    String itemId = inventory.get(i);
                    inventorySystem.removeItem(playerEntity, itemId);
                    inventory.set(i, null);
                    break;
                }
            }
        }
    }
}
