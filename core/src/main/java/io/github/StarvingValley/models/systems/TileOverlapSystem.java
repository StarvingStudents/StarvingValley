package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;

import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.TileOverlapComponent;
import io.github.StarvingValley.utils.TileUtils;

public class TileOverlapSystem extends EntitySystem implements EntityListener {
    private Family family = Family.all(PositionComponent.class, SizeComponent.class, TileOverlapComponent.class)
            .get();

    @Override
    public void addedToEngine(Engine engine) {
        engine.addEntityListener(family, this);
        for (Entity e : engine.getEntitiesFor(family)) {
            TileUtils.updateOverlappingTiles(e);
        }
    }

    @Override
    public void entityAdded(Entity entity) {
        TileUtils.updateOverlappingTiles(entity);
    }

    @Override
    public void entityRemoved(Entity entity) {
        
    }
}
