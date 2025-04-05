package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.SyncComponent;
import io.github.StarvingValley.models.components.TileOccupierComponent;
import io.github.StarvingValley.models.components.WorldLayerComponent;
import io.github.StarvingValley.models.types.WorldLayer;

public class SoilFactory {
  public static Entity createSoil(float x, float y) {
    Entity soil = new Entity();
    soil.add(new PositionComponent(x, y));
    soil.add(new WorldLayerComponent(WorldLayer.SOIL));
        soil.add(new TileOccupierComponent());
    soil.add(new SyncComponent());
    soil.add(new SizeComponent(1f, 1f));
    soil.add(new SpriteComponent("dirt.png"));
    return soil;
  }
}
