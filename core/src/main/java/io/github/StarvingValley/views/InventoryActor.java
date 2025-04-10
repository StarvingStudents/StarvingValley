package io.github.StarvingValley.views;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import java.util.Map;
import io.github.StarvingValley.models.components.InventoryComponent;

public class InventoryActor extends Actor {
    private BitmapFont font;
    private Texture headerTexture;   // Background for header
    private Texture slotTexture;     // Background for each slot
    private Map<String, Integer> items; // Current inventory items
    private String[] slotItemTexts;  // Texts for each inventory slot

    private final int numSlots = 5;
    private final float headerHeight = 50; // height in pixels for the header
    private final float slotPadding = 10;  // space between slot boxes

    // Used to measure text width/height for centering.
    private GlyphLayout layout;

    public InventoryActor() {
        font = new BitmapFont();
        layout = new GlyphLayout();

        // Create a header texture (e.g., dark blue)
        Pixmap headerPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        headerPixmap.setColor(new Color(0.2f, 0.2f, 0.5f, 1f));
        headerPixmap.fill();
        headerTexture = new Texture(headerPixmap);
        headerTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        headerPixmap.dispose();

        // Create a slot texture (e.g., gray)
        Pixmap slotPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        slotPixmap.setColor(new Color(0.3f, 0.3f, 0.3f, 1f));
        slotPixmap.fill();
        slotTexture = new Texture(slotPixmap);
        slotTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        slotPixmap.dispose();

        // Prepare an array to hold text per slot.
        slotItemTexts = new String[numSlots];
        // Initialize to empty.
        for (int i = 0; i < numSlots; i++) {
            slotItemTexts[i] = "";
        }
    }

    /**
     * Updates the inventory data.
     * Here you can extract the inventory items from the player's InventoryComponent.
     */
    public void updateInventory(Entity playerEntity) {
        InventoryComponent inv = playerEntity.getComponent(InventoryComponent.class);
        if (inv == null) {
            // No inventory data.
            for (int i = 0; i < numSlots; i++) {
                slotItemTexts[i] = "";
            }
            return;
        }

        else if (inv != null) {
            inv.addItem("coin_id");  // Add 1 coins
            inv.addItem("coin_id");
            inv.addItem("sword_id"); // Add 1 sword
            inv.addItem("crop_id");  // Add 1 crop
            inv.addItem("wall_id");     // Add 1 wall
            inv.addItem("wood_id");     // Add 1 wood
            inv.addItem("shield_id");   // Add 1 shield
            inv.addItem("potion_id");   // Add 1 potion
            inv.addItem("coin_id");  // Add 1 coins
            inv.addItem("coin_id");
            Gdx.app.log("Inventory", inv.getItems().toString());

        }

        int index = 0;
        for (Map.Entry<String, Integer> entry : inv.getItems().entrySet()) {
            if (index >= numSlots) break;
            String itemId = entry.getKey();
            int quantity = entry.getValue();
            slotItemTexts[index] = getDisplayText(itemId, quantity);
            index++;
        }
        // Clear remaining slots if any
        while (index < numSlots) {
            slotItemTexts[index] = "";
            index++;
        }
    }

    // Helper: convert an item ID to a display text.
    private String getDisplayText(String itemId, int quantity) {
        String display;
        switch (itemId) {
            case "coin_id": display = "Coins"; break; //Replace with icon.png
            case "sword_id": display = "Sword"; break;
            case "crop_id": display = "Crops"; break;
            case "wall_id": display = "Wall"; break;
            case "wood_id": display = "Wood"; break;
            case "shield_id": display = "Shield"; break;
            case "potion_id": display = "Potion"; break;
            default: display = "Unknown";
        }
        return display + " x" + quantity;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Draw header area at the top of the actor.
        float x = getX();
        float y = getY();
        float width = getWidth();
        // Draw header background
        batch.draw(headerTexture, x, y + getHeight() - headerHeight, width, headerHeight);

        // Draw header text "INVENTORY" centered
        String headerText = "INVENTORY";
        layout.setText(font, headerText);
        float headerTextWidth = layout.width;
        float headerTextHeight = layout.height;
        font.draw(batch, headerText, x + (width - headerTextWidth) / 2, y + getHeight() - (headerHeight - headerTextHeight) / 2);

        // Calculate slot dimensions.
        // We'll divide the remaining height equally by 1 row, with fixed number of slots.
        float availableHeight = getHeight() - headerHeight - slotPadding * 2;
        float slotWidth = (width - (numSlots + 1) * slotPadding) / numSlots;
        float slotHeight = availableHeight;

        // Starting position for slots
        float slotY = y + slotPadding;
        float slotX = x + slotPadding;

        // Draw each slot (background and text)
        for (int i = 0; i < numSlots; i++) {
            // Draw slot background (box)
            batch.draw(slotTexture, slotX, slotY, slotWidth, slotHeight);

            // Draw item text if available in this slot.
            String text = slotItemTexts[i];
            if (text != null && !text.isEmpty()) {
                layout.setText(font, text);
                float textWidth = layout.width;
                float textHeight = layout.height;
                // Center the text within the slot.
                font.draw(batch, text, slotX + (slotWidth - textWidth) / 2, slotY + (slotHeight + textHeight) / 2);
            }
            // Move to the next slot (horizontally)
            slotX += slotWidth + slotPadding;
        }
    }

    public void dispose() {
        font.dispose();
        headerTexture.dispose();
        slotTexture.dispose();
    }
}
