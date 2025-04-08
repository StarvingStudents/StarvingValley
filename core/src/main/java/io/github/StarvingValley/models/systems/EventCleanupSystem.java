package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.EntitySystem;

import io.github.StarvingValley.models.types.GameContext;

public class EventCleanupSystem extends EntitySystem {
    private GameContext context;

    public EventCleanupSystem(GameContext context) {
        this.context = context;
    }

    @Override
    public void update(float delta) {
        context.eventBus.advanceFrame();
    }
}
