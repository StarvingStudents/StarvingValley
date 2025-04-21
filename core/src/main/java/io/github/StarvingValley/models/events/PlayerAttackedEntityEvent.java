package io.github.StarvingValley.models.events;

import com.badlogic.ashley.core.Entity;

import io.github.StarvingValley.models.interfaces.Event;

public class PlayerAttackedEntityEvent implements Event {
    private final Entity entity;

    public PlayerAttackedEntityEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
} 