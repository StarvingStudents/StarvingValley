package io.github.StarvingValley.controllers;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.HotbarComponent;
import io.github.StarvingValley.models.components.InventoryComponent;
import io.github.StarvingValley.models.events.EntityUpdatedEvent;
import io.github.StarvingValley.models.events.InventoryCloseEvent;
import io.github.StarvingValley.models.events.InventoryOpenEvent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.Inventory;
import io.github.StarvingValley.models.types.InventorySlot;
import io.github.StarvingValley.views.InventoryView;

public class InventoryController {
    private final SpriteBatch batch;
    private final GameContext context;
    private InventoryView inventoryView;

    private Inventory inventory;

    private boolean inventoryIsVisible = false;
    private boolean hotbarIsVisible = false;

    private final int SLOT_SIZE;

    public InventoryController(GameContext context) {
        this.context = context;
        this.batch = new SpriteBatch();
        this.inventoryView = new InventoryView(this, context);

        int tileWidth = Gdx.graphics.getWidth() / Config.CAMERA_TILES_WIDE;
        SLOT_SIZE = (int) (tileWidth * Config.INVENTORY_SLOT_TILE_SIZE_MULTIPLIER);
    }

    public void setInventoryVisible(boolean visible) {
        this.inventoryIsVisible = visible;
    }

    public boolean isInventoryVisible() {
        return this.inventoryIsVisible;
    }

    public boolean isHotbarVisible() {
        return hotbarIsVisible;
    }

    public void setHotbarIsVisible(boolean hotbarIsVisible) {
        this.hotbarIsVisible = hotbarIsVisible;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        inventoryView.setInventory(inventory);
    }

    public Vector2 calculateInventoryPosition(Inventory inventory) {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        return new Vector2(
                (screenWidth - inventory.width * SLOT_SIZE) / 2f,
                (screenHeight + inventory.height * SLOT_SIZE) / 2f + SLOT_SIZE / 2);
    }

    public boolean isInsideInventory(Vector2 screenPos, Inventory inv, Vector2 origin) {
        float width = inv.width * SLOT_SIZE;
        float height = inv.height * SLOT_SIZE;

        return screenPos.x >= origin.x &&
                screenPos.x < origin.x + width &&
                screenPos.y >= origin.y - height &&
                screenPos.y < origin.y;
    }
    
    public Vector2 calculateHotbarPosition(Inventory hotbar) {
        int screenWidth = Gdx.graphics.getWidth();

        return new Vector2(
                (screenWidth - hotbar.width * SLOT_SIZE) / 2f,
                SLOT_SIZE + SLOT_SIZE / 2);
    }

    public void update() {
        handleEvents();

        if (inventoryView == null)
            return;
        inventoryView.update();
    }

    public void render() {
        if (inventoryView == null)
            return;

        batch.begin();
        inventoryView.render(batch);
        batch.end();
    }

    public boolean isInventoryIsVisible() {
        return inventoryIsVisible;
    }

    // TODO: Sync
    public void onDragCompleted(
            InventorySlot draggedSlot,
            Inventory sourceInventory,
            Vector2 dropScreenPos) {

        Inventory[] targets = new Inventory[] { getHotbar(), getInventory() };

        for (Inventory target : targets) {
            if (target == null)
                continue;

            Vector2 origin = target == getInventory()
                    ? calculateInventoryPosition(target)
                    : calculateHotbarPosition(target);

            if (!isInsideInventory(dropScreenPos, target, origin)) {
                continue;
            }

            GridPoint2 toSlot = screenToSlot(dropScreenPos, origin, target, SLOT_SIZE);
            if (toSlot == null)
                continue;

            if (sourceInventory == target &&
                    draggedSlot.x == toSlot.x &&
                    draggedSlot.y == toSlot.y) {
                return;
            }

            InventorySlot destination = target.getSlotAt((int) toSlot.x, (int) toSlot.y);

            if (destination == null) {
                sourceInventory.slots.remove(draggedSlot);
                target.slots.add(new InventorySlot(
                        draggedSlot.itemStack.type,
                        draggedSlot.itemStack.quantity,
                        (int) toSlot.x,
                        (int) toSlot.y));
            } else {
                sourceInventory.slots.remove(draggedSlot);
                target.slots.remove(destination);

                target.slots.add(new InventorySlot(
                        draggedSlot.itemStack.type,
                        draggedSlot.itemStack.quantity,
                        (int) toSlot.x,
                        (int) toSlot.y));

                sourceInventory.slots.add(new InventorySlot(
                        destination.itemStack.type,
                        destination.itemStack.quantity,
                        (int) draggedSlot.x,
                        (int) draggedSlot.y));
            }

            context.eventBus.publish(new EntityUpdatedEvent(context.player));

            return;
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    private void handleEvents() {
        List<InventoryOpenEvent> openEvents = context.eventBus.getEvents(InventoryOpenEvent.class);
        if (!openEvents.isEmpty()) {
            InventoryOpenEvent openEvent = openEvents.get(openEvents.size() - 1);

            InventoryComponent inventoryComponent = Mappers.inventory.get(openEvent.targetEntity);
            HotbarComponent hotbarComponent = Mappers.hotbar.get(openEvent.targetEntity);

            if (inventoryComponent != null && hotbarComponent != null) {
                setInventory(inventoryComponent.inventory);
                setInventoryVisible(true);
            }
        }

        List<InventoryCloseEvent> closeEvents = context.eventBus.getEvents(InventoryCloseEvent.class);
        if (!closeEvents.isEmpty()) {
            setInventoryVisible(false);
        }

        // TODO: Events for hotbar?
    }

    public GridPoint2 screenToSlot(Vector2 screen, Vector2 origin, Inventory inv, int tileWidth) {
        float offsetX = screen.x - origin.x;
        float offsetY = origin.y - screen.y;

        int x = (int) (offsetX / tileWidth);
        int y = (int) (offsetY / tileWidth);

        if (x < 0 || x >= inv.width || y < 0 || y >= inv.height)
            return null;
        return new GridPoint2(x, y);
    }

    public Inventory getHotbar() {
        if (!isHotbarVisible())
            return null;
        if (context.player == null || !Mappers.hotbar.has(context.player))
            return null;
        return Mappers.hotbar.get(context.player).hotbar;
    }

    public int getSlotSize() {
        return SLOT_SIZE;
    }
}
