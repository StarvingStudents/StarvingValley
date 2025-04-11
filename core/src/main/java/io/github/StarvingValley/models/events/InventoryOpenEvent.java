package io.github.StarvingValley.models.events;

import com.badlogic.ashley.core.Entity;

import io.github.StarvingValley.models.Interfaces.Event;

public class InventoryOpenEvent implements Event {
    public Entity targetEntity;

    public InventoryOpenEvent(Entity entity) {
        this.targetEntity = entity;
    }
}
