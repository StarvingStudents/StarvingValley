package io.github.StarvingValley.models.events;

import com.badlogic.ashley.core.Entity;

public class EntityRemovedEvent extends SyncIdEvent {
    public EntityRemovedEvent(Entity removedEntity) {
        super(removedEntity);
    }
}
