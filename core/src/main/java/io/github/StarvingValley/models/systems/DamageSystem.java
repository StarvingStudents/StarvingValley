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
import io.github.StarvingValley.models.components.DamageComponent;
import io.github.StarvingValley.config.Config;

public class DamageSystem extends IteratingSystem {
    private final GameContext context;

    public DamageSystem(GameContext context) {
        super(Family.all(ClickedComponent.class, DurabilityComponent.class, DamageComponent.class).get());
        this.context = context;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // Check distance
        PositionComponent playerPos = Mappers.position.get(context.player);
        PositionComponent entityPos = Mappers.position.get(entity);
        DamageComponent damage = Mappers.damage.get(entity);
        
        if (!isPlayerNearEntity(playerPos, entityPos, damage.attackRange)) return;

        // Update and check cooldown
        damage.cooldownTimer += deltaTime;
        float cooldownDuration = 1f / damage.attackSpeed;
        if (damage.cooldownTimer < cooldownDuration) return;

        // Apply damage
        DurabilityComponent durability = Mappers.durability.get(entity);
        durability.reduceHealth(damage.damageAmount);

        // Publish events
        context.eventBus.publish(new PlayerAttackedEntityEvent(entity));
        context.eventBus.publish(new EntityUpdatedEvent(entity));

        // Reset cooldown
        damage.cooldownTimer = 0f;
    }

    private boolean isPlayerNearEntity(PositionComponent playerPos, PositionComponent entityPos, float attackRange) {
        float distance = playerPos.position.dst(entityPos.position);
        return distance < attackRange;
    }
}
