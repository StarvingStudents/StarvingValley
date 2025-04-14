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
import io.github.StarvingValley.models.components.CooldownTimerComponent;

public class DamageSystem extends IteratingSystem {
    private final GameContext context;

    public DamageSystem(GameContext context) {
        super(Family.all(ClickedComponent.class, DurabilityComponent.class).get());
        this.context = context;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // Get player's components
        DamageComponent playerDamage = Mappers.damage.get(context.player);
        CooldownTimerComponent playerCooldown = Mappers.cooldownTimer.get(context.player);
        if (playerDamage == null || playerCooldown == null) return;

        // Check distance
        PositionComponent playerPos = Mappers.position.get(context.player);
        PositionComponent entityPos = Mappers.position.get(entity);
        
        if (!isPlayerNearEntity(playerPos, entityPos, playerDamage.attackRange)) return;

        // Update and check cooldown
        playerCooldown.cooldownTimer += deltaTime;
        if (!playerCooldown.isReady()) return;

        // Apply damage
        DurabilityComponent durability = Mappers.durability.get(entity);
        durability.reduceHealth(playerDamage.damageAmount);

        // Publish events
        context.eventBus.publish(new PlayerAttackedEntityEvent(entity));
        context.eventBus.publish(new EntityUpdatedEvent(entity));

        // Reset cooldown
        playerCooldown.reset();
    }

    private boolean isPlayerNearEntity(PositionComponent playerPos, PositionComponent entityPos, float attackRange) {
        float distance = playerPos.position.dst(entityPos.position);
        return distance < attackRange;
    }
}
