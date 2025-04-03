package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.EntitySystem;

import io.github.StarvingValley.models.events.EventBus;

public class EventCleanupSystem extends EntitySystem {
    private final EventBus eventBus;

    public EventCleanupSystem(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void update(float delta) {
        eventBus.advanceFrame();
    }
}
