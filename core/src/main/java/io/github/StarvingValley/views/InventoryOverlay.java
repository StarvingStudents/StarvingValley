package io.github.StarvingValley.views;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.Map;
import java.util.Map.Entry;
import io.github.StarvingValley.models.components.InventoryComponent;

public class InventoryOverlay {
    private Stage stage;
    private Table table;
    private BitmapFont font;
    private Label.LabelStyle labelStyle;

    public InventoryOverlay(Viewport viewport) {
        // Create a new Stage for the inventory UI using the given viewport
        stage = new Stage(viewport);

        font = new BitmapFont();
        labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        // Create a Table to organize your inventory items
        table = new Table();
        float tableWidth = viewport.getWorldWidth() * 0.8f;
        float tableHeight = viewport.getWorldHeight() * 0.8f;
        table.setSize(tableWidth, tableHeight);

        // Center the table on the stage.
        table.setPosition((viewport.getWorldWidth() - tableWidth) / 2, (viewport.getWorldHeight() - tableHeight) / 2);

        table.setDebug(true); // Remove or set to false in production.

        stage.addActor(table);
    }

    public Stage getStage() {
        return stage;
    }

    // Helper method to convert an item ID into a display name.
    private String getItemDisplayName(String itemId) {
        if (itemId == null) return "";
        switch (itemId) {
            case "coin_id": return "Coins";
            case "sword_id": return "Sword";
            case "crop_id": return "Crops";
            default: return "Unknown Item";
        }
    }

    // Call this method to update the UI with the current inventory data.
    public void update(Entity playerEntity) {
        table.clear();

        // Retrieve the inventory data from the player's InventoryComponent
        InventoryComponent invComponent = (InventoryComponent) playerEntity.getComponent(InventoryComponent.class);
        if (invComponent == null) {
            Gdx.app.log("InventoryOverlay", "No InventoryComponent found");
            return;
        }

        invComponent.addItem("coin_id");  // Initial item: coin
        invComponent.addItem("coin_id");  // Adding 1 more coin
        invComponent.addItem("sword_id");    // Initial item: sword

        Map<String, Integer> inventoryMap = invComponent.getItems();
        Gdx.app.log("InventoryOverlay", "Inventory size: " + inventoryMap.size());


        // For each item in the inventory, create a Label and add it to the table
        for (Entry<String, Integer> entry : inventoryMap.entrySet()) {
            String itemId = entry.getKey();
            int quantity = entry.getValue();
            String displayText = getItemDisplayName(itemId) + " x" + quantity;
            Gdx.app.log("InventoryOverlay", "Adding item: " + displayText);


            Label label = new Label(displayText, labelStyle);
            table.add(label).pad(5);
            table.row();
        }
    }

    // Render the inventory UI. This should be called from GameScreen when inventory is visible.
    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    // Dispose resources when no longer needed
    public void dispose() {
        stage.dispose();
        font.dispose();
    }
}
