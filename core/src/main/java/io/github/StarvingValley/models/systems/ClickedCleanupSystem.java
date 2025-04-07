package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.components.ClickedComponent;

public class ClickedCleanupSystem extends IteratingSystem {
    public ClickedCleanupSystem() {
        super(Family.all(ClickedComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float delta) {
        entity.remove(ClickedComponent.class);
    }
}
