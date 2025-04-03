package io.github.StarvingValley.models.events;

import com.badlogic.ashley.core.Entity;

public class EntityAddedEvent extends EntityEvent {

    public EntityAddedEvent(Entity entity) {
        super(entity);
    }
}
