package io.github.StarvingValley.models.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Entity;

import io.github.StarvingValley.models.components.BuildableComponent;
import io.github.StarvingValley.models.components.DropComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.SyncComponent;
import io.github.StarvingValley.models.components.TileOccupierComponent;
import io.github.StarvingValley.models.components.WorldLayerComponent;
import io.github.StarvingValley.models.types.ItemStack;
import io.github.StarvingValley.models.types.PrefabType;
import io.github.StarvingValley.models.types.WorldLayer;

public class SoilFactory {
  public static Entity createSoil() {
    Entity soil = new Entity();
    soil.add(new WorldLayerComponent(WorldLayer.SOIL));
    soil.add(new TileOccupierComponent());
    soil.add(new SyncComponent());
    soil.add(new SizeComponent(1f, 1f));
    soil.add(new SpriteComponent("dirt.png"));
    soil.add(new DropComponent(new ArrayList<>(List.of(new ItemStack(PrefabType.SOIL, 1)))));
    soil.add(new BuildableComponent(PrefabType.SOIL));

    return soil;
  }
}
