package io.github.StarvingValley.views;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public class InventoryOverlay {
    private Stage stage;
    private InventoryActor inventoryActor;

    public InventoryOverlay(Viewport viewport) {
        // Create the stage for the UI.
        stage = new Stage(viewport);

        // Create and position the InventoryActor.
        inventoryActor = new InventoryActor();
        // Set the size of the InventoryActor to cover the bottom 30% of the screen.
        inventoryActor.setSize(viewport.getWorldWidth(), viewport.getWorldHeight() * 0.3f);
        // Position it at the bottom of the screen.
        inventoryActor.setPosition(0, 0);

        stage.addActor(inventoryActor);
    }

    // Expose the stage if needed to set input processor.
    public Stage getStage() {
        return stage;
    }

    /**
     * Updates the InventoryActor with the current inventory data from the player.
     */
    public void update(Entity playerEntity) {
        inventoryActor.updateInventory(playerEntity);
    }

    /**
     * Renders the inventory overlay.
     */
    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
        inventoryActor.dispose();
    }
}
