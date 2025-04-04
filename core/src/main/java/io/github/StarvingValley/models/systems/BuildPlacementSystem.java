package io.github.StarvingValley.models.systems;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.GridPoint2;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.BuildPreviewComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.events.BuildPreviewClickedEvent;
import io.github.StarvingValley.models.events.EntityAddedEvent;
import io.github.StarvingValley.models.events.EntityPlacedEvent;
import io.github.StarvingValley.models.events.EventBus;

public class BuildPlacementSystem extends EntitySystem {
    private final EventBus eventBus;

    public BuildPlacementSystem(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void update(float deltaTime) {
        List<BuildPreviewClickedEvent> events = eventBus.getEvents(BuildPreviewClickedEvent.class);
        for (BuildPreviewClickedEvent event : events) {
            events.add(event);
        }

        Set<Entity> uniqueEntities = new HashSet<>();

        for (BuildPreviewClickedEvent event : events) {
            uniqueEntities.add(event.getEntity());
        }

        for (Entity previewEntity : uniqueEntities) {
            placeEntityFromPreviewEntity(previewEntity);
        }
    }

    private void placeEntityFromPreviewEntity(Entity previewEntity) {
        BuildPreviewComponent buildPreview = Mappers.buildPreview.get(previewEntity);
        PositionComponent position = Mappers.position.get(previewEntity);

        if (buildPreview == null || buildPreview.isBlocked) {
            return;
        }

        Entity createdEntity = buildPreview.entityFactory.createAt(
                new GridPoint2((int) position.position.x, (int) position.position.y));

        getEngine().addEntity(createdEntity);

        // TODO: Inventory should listen to this, and remove the entity/lower
        // counter from the inventory. If there's no more items of this type to place,
        // exit build mode by removing the buildpreview entity from the engine.
        // Inventory system could probably send an event to this system to tell it to
        // stop placing that item. Then that inventory/publisher should be before
        // BuildPlacementSystem in the engine
        eventBus.publish(new EntityPlacedEvent(createdEntity));
        eventBus.publish(new EntityAddedEvent(createdEntity));
    }
}
