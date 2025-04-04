package io.github.StarvingValley.models.systems;

import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;

import io.github.StarvingValley.models.components.SyncDeletionRequestComponent;
import io.github.StarvingValley.models.components.UnsyncedComponent;
import io.github.StarvingValley.models.events.EntityAddedEvent;
import io.github.StarvingValley.models.events.EntityRemovedEvent;
import io.github.StarvingValley.models.events.EntityUpdatedEvent;
import io.github.StarvingValley.models.events.EventBus;

public class SyncMarkingSystem extends EntitySystem {
    private final EventBus eventBus;

    public SyncMarkingSystem(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void update(float deltaTime) {
        markAddedEntitiesUnsynced();
        markUpdatedEntitiesUnsynced();
        markRemovedEntitiesForDeletion();
    }

    private void markAddedEntitiesUnsynced() {
        List<EntityAddedEvent> addEvents = eventBus.getEvents(EntityAddedEvent.class);

        for (EntityAddedEvent event : addEvents) {
            event.getEntity().add(new UnsyncedComponent());
        }
    }

    private void markUpdatedEntitiesUnsynced() {
        List<EntityUpdatedEvent> updateEvents = eventBus.getEvents(EntityUpdatedEvent.class);

        for (EntityUpdatedEvent event : updateEvents) {
            event.getEntity().add(new UnsyncedComponent());
        }
    }

    private void markRemovedEntitiesForDeletion() {
        List<EntityRemovedEvent> removeEvents = eventBus.getEvents(EntityRemovedEvent.class);

        for (EntityRemovedEvent event : removeEvents) {
            Entity deletionMarkerEntity = getEngine().createEntity();
            deletionMarkerEntity.add(new SyncDeletionRequestComponent(event.getEntitySyncId()));
            getEngine().addEntity(deletionMarkerEntity);
        }
    }
}
