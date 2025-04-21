package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.EconomyComponent;
import io.github.StarvingValley.models.components.InventoryItemComponent;
import io.github.StarvingValley.models.components.TradeableComponent;
import io.github.StarvingValley.models.events.AddItemToInventoryEvent;
import io.github.StarvingValley.models.events.EntityUpdatedEvent;
import io.github.StarvingValley.models.events.NotificationEvent;
import io.github.StarvingValley.models.types.GameContext;

public class TradingSystem extends IteratingSystem {
    private final GameContext context;

    public TradingSystem(GameContext context) {
        super(
                Family.all(
                        TradeableComponent.class,
                        InventoryItemComponent.class,
                        ClickedComponent.class)
                        .get());
        this.context = context;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (context.player == null)
            return;

        InventoryItemComponent trading = Mappers.inventoryItem.get(entity);
        TradeableComponent tradeable = Mappers.tradeable.get(entity);
        EconomyComponent economy = Mappers.economy.get(context.player);

        if (tradeable.price > economy.balance) {
            context.eventBus.publish(new NotificationEvent("Missing " + (tradeable.price - economy.balance) + " coins"));
            return;
        }

        economy.balance -= tradeable.price;

        context.eventBus.publish(new AddItemToInventoryEvent(context.player, trading.type, 1));
        context.eventBus.publish(new EntityUpdatedEvent(context.player));

        context.eventBus.publish(new NotificationEvent("Bought item!"));
    }
}