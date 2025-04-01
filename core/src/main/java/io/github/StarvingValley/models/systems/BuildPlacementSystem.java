package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.GridPoint2;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.BuildPreviewComponent;
import io.github.StarvingValley.models.components.PlaceRequestComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.utils.SyncUtils;

public class BuildPlacementSystem extends IteratingSystem {
    public BuildPlacementSystem() {
        super(Family.all(BuildPreviewComponent.class, PlaceRequestComponent.class, PositionComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BuildPreviewComponent buildPreview = Mappers.buildPreview.get(entity);
        PositionComponent position = Mappers.position.get(entity);

        if (buildPreview.isBlocked) {
            return;
        }

        Engine engine = getEngine();

        Entity createdEntity = buildPreview.entityFactory
                .createAt(new GridPoint2((int) position.position.x, (int) position.position.y));
        SyncUtils.markUnsynced(createdEntity);

        engine.addEntity(createdEntity);

        // TODO: When inventory is implemented we can change this to check if there are
        // any more of that buildable available, and if not:
        // engine.removeEntity(entity);
        entity.remove(PlaceRequestComponent.class);
    }
}
