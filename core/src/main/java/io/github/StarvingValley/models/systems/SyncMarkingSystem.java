package io.github.StarvingValley.models.systems;

import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;

import io.github.StarvingValley.models.components.SyncDeletionRequestComponent;
import io.github.StarvingValley.models.components.UnsyncedComponent;
import io.github.StarvingValley.models.events.EntityAddedEvent;
import io.github.StarvingValley.models.events.EntityRemovedEvent;
import io.github.StarvingValley.models.events.EntityUpdatedEvent;
import io.github.StarvingValley.models.types.GameContext;

public class SyncMarkingSystem extends EntitySystem {
    private GameContext context;

    public SyncMarkingSystem(GameContext context) {
        this.context = context;
    }

    @Override
    public void update(float deltaTime) {
        markAddedEntitiesUnsynced();
        markUpdatedEntitiesUnsynced();
        markRemovedEntitiesForDeletion();
    }

    private void markAddedEntitiesUnsynced() {
        List<EntityAddedEvent> addEvents = context.eventBus.getEvents(EntityAddedEvent.class);

        for (EntityAddedEvent event : addEvents) {
            event.getEntity().add(new UnsyncedComponent());
        }
    }

    private void markUpdatedEntitiesUnsynced() {
        List<EntityUpdatedEvent> updateEvents = context.eventBus.getEvents(EntityUpdatedEvent.class);

        for (EntityUpdatedEvent event : updateEvents) {
            event.getEntity().add(new UnsyncedComponent());
        }
    }

    private void markRemovedEntitiesForDeletion() {
        List<EntityRemovedEvent> removeEvents = context.eventBus.getEvents(EntityRemovedEvent.class);

        for (EntityRemovedEvent event : removeEvents) {
            Entity deletionMarkerEntity = getEngine().createEntity();
            deletionMarkerEntity.add(new SyncDeletionRequestComponent(event.getEntitySyncId()));
            getEngine().addEntity(deletionMarkerEntity);
        }
    }
}
