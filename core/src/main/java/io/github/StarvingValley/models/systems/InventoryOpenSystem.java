package io.github.StarvingValley.models.systems;

import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.InventoryToggleButtonComponent;
import io.github.StarvingValley.models.events.InventoryCloseEvent;
import io.github.StarvingValley.models.events.InventoryOpenEvent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.InventoryInfo;
import io.github.StarvingValley.utils.BuildUtils;
import io.github.StarvingValley.utils.InventoryUtils;

public class InventoryOpenSystem extends EntitySystem {
    private final GameContext context;

    public InventoryOpenSystem(GameContext context) {
        this.context = context;
    }

    @Override
    public void update(float deltaTime) {
        List<InventoryOpenEvent> openEvents = context.eventBus.getEvents(InventoryOpenEvent.class);
        List<InventoryCloseEvent> closeEvents = context.eventBus.getEvents(InventoryCloseEvent.class);

        for (InventoryOpenEvent openEvent : openEvents) {
            if (openEvent.inventory.isOpen)
                continue;

            open(openEvent.inventory);
        }

        for (InventoryCloseEvent closeEvent : closeEvents) {
            close(closeEvent.inventory);
        }

        ImmutableArray<Entity> clickedToggleButtons = getEngine()
                .getEntitiesFor(Family.all(InventoryToggleButtonComponent.class, ClickedComponent.class).get());

        for (Entity clickedToggleButton : clickedToggleButtons) {
            InventoryToggleButtonComponent toggle = Mappers.inventoryToggleButton.get(clickedToggleButton);
            if (toggle.inventoryToToggle.isOpen) {
                close(toggle.inventoryToToggle);
            } else {
                open(toggle.inventoryToToggle);
            }
        }
    }

    private void close(InventoryInfo inventory) {
        InventoryUtils.closeInventory(getEngine(), inventory);
    }

    private void open(InventoryInfo inventory) {
        InventoryUtils.openInventory(getEngine(), inventory, inventory.inventoryType);
        InventoryUtils.unselectSelectedHotbarItems(getEngine());
        BuildUtils.disableBuildPreview(getEngine());
    }
}
