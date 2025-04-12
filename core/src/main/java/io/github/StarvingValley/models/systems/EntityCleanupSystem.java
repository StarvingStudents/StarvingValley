package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.DurabilityComponent;
import io.github.StarvingValley.models.events.EntityRemovedEvent;
import io.github.StarvingValley.models.types.GameContext;

public class EntityCleanupSystem extends IteratingSystem {
    private final GameContext context;

    public EntityCleanupSystem(GameContext context) {
        super(Family.all(DurabilityComponent.class).get());
        this.context = context;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        DurabilityComponent durability = Mappers.durability.get(entity);
        
        if (durability.getHealth() <= 0) {
            context.eventBus.publish(new EntityRemovedEvent(entity));
            getEngine().removeEntity(entity);
        }
    }
} 