package io.github.StarvingValley.models.events;

import com.badlogic.ashley.core.Entity;

public class EntityPlacedEvent extends EntityEvent {
    public EntityPlacedEvent(Entity placedEntity) {
        super(placedEntity);
    }
}
