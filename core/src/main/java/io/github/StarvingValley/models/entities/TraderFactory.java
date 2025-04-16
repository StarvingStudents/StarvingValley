package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;

import io.github.StarvingValley.models.components.ClickableComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.TradingComponent;
import io.github.StarvingValley.models.components.WorldLayerComponent;
import io.github.StarvingValley.models.types.ItemTrade;
import io.github.StarvingValley.models.types.PrefabType;
import io.github.StarvingValley.models.types.WorldLayer;

public class TraderFactory {
    public static Entity create(float x, float y, PrefabType trade, int price) {
        Entity trader = new Entity();
        trader
                .add(new SizeComponent(1f, 1f))
                .add(new SpriteComponent("DogBasic.png"))
                .add(new TradingComponent(new ItemTrade(trade, price)))
                .add(new PositionComponent(x, y))
                .add(new WorldLayerComponent(WorldLayer.CHARACTER))
                .add(new ClickableComponent());

        return trader;
    }
}
