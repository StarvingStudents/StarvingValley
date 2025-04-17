package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.EconomyComponent;
import io.github.StarvingValley.models.components.TradingComponent;
import io.github.StarvingValley.models.events.AddItemToInventoryEvent;
import io.github.StarvingValley.models.types.GameContext;

public class TradingSystem extends IteratingSystem {
    private final GameContext context;

    public TradingSystem(GameContext context) {
        super(
                Family.all(
                        TradingComponent.class,
                        ClickedComponent.class)
                        .get());
        this.context = context;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (context.player == null)
            return;

        TradingComponent trading = Mappers.trading.get(entity);
        EconomyComponent economy = Mappers.economy.get(context.player);

        if (trading.trade.cost > economy.balance) {
            return;
        }

        economy.balance -= trading.trade.cost;

        context.eventBus.publish(new AddItemToInventoryEvent(context.player, trading.trade.type, 1));
    }
}
