package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.InventoryComponent;
import io.github.StarvingValley.models.components.TradingComponent;
import io.github.StarvingValley.models.events.InventoryCloseEvent;
import io.github.StarvingValley.models.events.InventoryOpenEvent;
import io.github.StarvingValley.models.types.GameContext;

public class TraderClickSystem extends IteratingSystem {
    private final GameContext context;

    public TraderClickSystem(GameContext context) {
        super(
                Family.all(
                        TradingComponent.class,
                        ClickedComponent.class,
                        InventoryComponent.class)
                        .get());
        this.context = context;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        InventoryComponent inventory = Mappers.inventory.get(entity);

        if (inventory.info.isOpen) {
            context.eventBus.publish(new InventoryCloseEvent(inventory.info));
        } else {
            context.eventBus.publish(new InventoryOpenEvent(inventory.info));
        }
    }
}
