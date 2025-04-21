package io.github.StarvingValley.models.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Entity;

import io.github.StarvingValley.models.components.BuildableComponent;
import io.github.StarvingValley.models.components.ClickableComponent;
import io.github.StarvingValley.models.components.DropComponent;
import io.github.StarvingValley.models.components.DurabilityComponent;
import io.github.StarvingValley.models.components.EnvironmentCollidableComponent;
import io.github.StarvingValley.models.components.PickupComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.SyncComponent;
import io.github.StarvingValley.models.components.TileOccupierComponent;
import io.github.StarvingValley.models.components.WorldLayerComponent;
import io.github.StarvingValley.models.types.ItemStack;
import io.github.StarvingValley.models.types.PrefabType;
import io.github.StarvingValley.models.types.WorldLayer;
import io.github.StarvingValley.config.Config;

public class WallFactory {
    public static Entity createWall() {
        Entity soil = new Entity();
        soil.add(new WorldLayerComponent(WorldLayer.STRUCTURE));
        soil.add(new TileOccupierComponent());
        soil.add(new SyncComponent());
        soil.add(new DurabilityComponent());
        soil.add(new EnvironmentCollidableComponent());
        soil.add(new ClickableComponent());
        soil.add(new SizeComponent(1, 1));
        soil.add(new SpriteComponent("wall.png"));
        soil.add(new DropComponent(new ArrayList<>(List.of(new ItemStack(PrefabType.WALL, 1)))));
        soil.add(new BuildableComponent(PrefabType.WALL));
        soil.add(new PickupComponent(Config.DEFAULT_PICKUP_RANGE));

        return soil;
    }
}
