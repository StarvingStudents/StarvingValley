package io.github.StarvingValley.models.events;

import com.badlogic.ashley.core.Entity;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.Interfaces.Event;
import io.github.StarvingValley.models.components.SyncComponent;

/**
 * Abstract Event for entity and entity sync id
 */
public abstract class EntityEvent implements Event {
    protected final Entity entity;
    protected final String entitySyncId;

    public EntityEvent(Entity entity) {
        this.entity = entity;

        SyncComponent syncComponent = Mappers.sync.get(entity);
        if (syncComponent != null) {
            this.entitySyncId = syncComponent.id;
        } else {
            this.entitySyncId = null;
        }
    }

    public Entity getEntity() {
        return entity;
    }

    public String getEntitySyncId() {
        return entitySyncId;
    }
}
