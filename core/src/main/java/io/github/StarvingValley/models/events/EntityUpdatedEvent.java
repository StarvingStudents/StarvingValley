package io.github.StarvingValley.models.events;

import com.badlogic.ashley.core.Entity;

public class EntityUpdatedEvent extends EntityEvent {
    public EntityUpdatedEvent(Entity entity) {
        super(entity);
    }    
}
