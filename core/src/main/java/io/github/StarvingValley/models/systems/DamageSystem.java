package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.DurabilityComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.events.PlayerAttackedEntityEvent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.events.EntityUpdatedEvent;

public class DamageSystem extends IteratingSystem {
    private final GameContext context;
    private final float damageAmount = 10f;
    private final float maxDamageDistance = 100.03f;
    private float cooldownTimer = 0f;
    private final float cooldownDuration = 0.4f; // Time between damage applications

    public DamageSystem(GameContext context) {
        super(Family.all(ClickedComponent.class, DurabilityComponent.class).get());
        this.context = context;
    }

    @Override
    public void update(float deltaTime) {
        cooldownTimer += deltaTime;
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // Check distance
        PositionComponent playerPos = Mappers.position.get(context.player);
        PositionComponent entityPos = Mappers.position.get(entity);
        if (!isPlayerNearEntity(playerPos, entityPos)) return;

        // Check cooldown
        if (cooldownTimer < cooldownDuration) return;

        // Apply damage
        DurabilityComponent durability = Mappers.durability.get(entity);
        durability.reduceHealth(damageAmount);

        // Publish events
        context.eventBus.publish(new PlayerAttackedEntityEvent(entity));
        context.eventBus.publish(new EntityUpdatedEvent(entity));

        // Reset cooldown
        cooldownTimer = 0f;
    }

    private boolean isPlayerNearEntity(PositionComponent playerPos, PositionComponent entityPos) {
        float distance = playerPos.position.dst(entityPos.position);
        return distance < maxDamageDistance;
    }
}
