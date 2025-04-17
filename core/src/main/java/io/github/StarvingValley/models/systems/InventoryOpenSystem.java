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
            if (InventoryUtils.isInventoryOpen(getEngine(), openEvent.inventory.inventoryId))
                continue;

            InventoryUtils.openInventory(getEngine(), openEvent.inventory, openEvent.isHotbar);
            InventoryUtils.unselectSelectedHotbarItems(getEngine());
            BuildUtils.disableBuildPreview(getEngine());
        }

        for (InventoryCloseEvent closeEvent : closeEvents) {
            InventoryUtils.closeInventory(getEngine(), closeEvent.inventory);
        }

        ImmutableArray<Entity> clickedToggleButtons = getEngine()
                .getEntitiesFor(Family.all(InventoryToggleButtonComponent.class, ClickedComponent.class).get());

        for (Entity clickedToggleButton : clickedToggleButtons) {
            InventoryToggleButtonComponent toggle = Mappers.inventoryToggleButton.get(clickedToggleButton);
            if (InventoryUtils.isInventoryOpen(getEngine(), toggle.inventoryToToggle.inventoryId)) {
                context.eventBus.publish(new InventoryCloseEvent(toggle.inventoryToToggle));
            } else {
                context.eventBus.publish(new InventoryOpenEvent(toggle.inventoryToToggle, toggle.inventoryToToggle.isHotbar));
            }
        }
    }
}
