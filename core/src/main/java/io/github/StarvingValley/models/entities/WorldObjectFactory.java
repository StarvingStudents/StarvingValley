package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;

import io.github.StarvingValley.models.components.EnvironmentCollidableComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.TileOccupierComponent;
import io.github.StarvingValley.models.components.TileOverlapComponent;
import io.github.StarvingValley.models.components.WorldLayerComponent;
import io.github.StarvingValley.models.dto.WorldObjectConfig;

public class WorldObjectFactory {

    // TODO: This really should be a bunch of different methods and i should trash
    // the config. maybe change to builder pattern?
    public static Entity createWorldObject(Rectangle rect, WorldObjectConfig config) {
        Entity entity = new Entity();

        entity.add(new PositionComponent(rect.x, rect.y));
        entity.add(new SizeComponent(rect.width, rect.height));
        entity.add(new TileOverlapComponent());

        if (config.blocksMovement) {
            entity.add(new EnvironmentCollidableComponent());
        }

        if (config.blocksPlacement) {
            entity.add(new TileOccupierComponent());
        }

        if (config.worldLayer != null) {
            entity.add(new WorldLayerComponent(config.worldLayer));
        }

        if (config.texture != null) {
            entity.add(new SpriteComponent(config.texture));
        }

        return entity;
    }

    // TODO: Add createFromWorldObjectDTO
}
