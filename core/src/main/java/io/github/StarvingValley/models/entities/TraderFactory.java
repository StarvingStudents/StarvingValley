package io.github.StarvingValley.models.entities;

import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ClickableComponent;
import io.github.StarvingValley.models.components.InventoryComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.TradingComponent;
import io.github.StarvingValley.models.components.WorldLayerComponent;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.types.InventoryInfo;
import io.github.StarvingValley.models.types.InventoryType;
import io.github.StarvingValley.models.types.ItemTrade;
import io.github.StarvingValley.models.types.WorldLayer;
import io.github.StarvingValley.utils.InventoryUtils;

public class TraderFactory {
    public static Entity create(float x, float y, String texturePath) {
        Entity trader = new Entity();
        trader
                .add(new SizeComponent(1f, 1f))
                .add(new SpriteComponent(texturePath))
                .add(new TradingComponent())
                .add(new InventoryComponent(new InventoryInfo(4, 1, InventoryType.TRADING)))
                .add(new PositionComponent(x, y))
                .add(new WorldLayerComponent(WorldLayer.CHARACTER))
                .add(new ClickableComponent());

        return trader;
    }

    public static Entity addTraderToEngine(Engine engine, EventBus eventBus, float x, float y, List<ItemTrade> trades, String texturePath) {
        Entity trader = create(x, y, texturePath);
        engine.addEntity(trader);

        InventoryComponent inventory = Mappers.inventory.get(trader);
        InventoryUtils.initializeTraderInventory(engine, inventory.info, trades, eventBus);

        return trader;
    }
}
