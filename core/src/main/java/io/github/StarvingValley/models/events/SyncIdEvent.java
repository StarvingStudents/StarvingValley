package io.github.StarvingValley.models.events;

import com.badlogic.ashley.core.Entity;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.SyncComponent;
import io.github.StarvingValley.models.interfaces.Event;

/**
 * Abstract Event for entity sync id
 */
public abstract class SyncIdEvent implements Event {
    protected final String entitySyncId;

    public SyncIdEvent(Entity entity) {
        SyncComponent syncComponent = Mappers.sync.get(entity);
        if (syncComponent != null) {
            this.entitySyncId = syncComponent.id;
        } else {
            this.entitySyncId = null;
        }
    }

    public String getEntitySyncId() {
        return entitySyncId;
    }
}
