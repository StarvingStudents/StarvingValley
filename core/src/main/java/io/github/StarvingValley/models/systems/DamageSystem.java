package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.DamageComponent;
import io.github.StarvingValley.models.components.DurabilityComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.events.EntityUpdatedEvent;
import io.github.StarvingValley.models.events.PlayerAttackedEntityEvent;
import io.github.StarvingValley.models.types.GameContext;

public class DamageSystem extends IteratingSystem {
    private final GameContext context;

    public DamageSystem(GameContext context) {
        super(Family.all(ClickedComponent.class, DurabilityComponent.class).get());
        this.context = context;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // Get player's damage component
        DamageComponent playerAttack = Mappers.damage.get(context.player);
        if (playerAttack == null)
            return;

        // Check distance
        PositionComponent playerPos = Mappers.position.get(context.player);
        PositionComponent entityPos = Mappers.position.get(entity);

        if (!isPlayerNearEntity(playerPos, entityPos, playerAttack.attackRange))
            return;

        if (!playerAttack.isReady())
            return;

        // Apply damage
        DurabilityComponent durability = Mappers.durability.get(entity);
        durability.reduceHealth(playerAttack.damageAmount);

        // Publish events
        context.eventBus.publish(new PlayerAttackedEntityEvent(entity));
        context.eventBus.publish(new EntityUpdatedEvent(entity));

        // Reset cooldown
        playerAttack.resetCooldown();

        if (durability.health <= 0) {
            getEngine().removeEntity(entity);
        }
    }

    private boolean isPlayerNearEntity(PositionComponent playerPos, PositionComponent entityPos, float attackRange) {
        float distance = playerPos.position.dst(entityPos.position);
        return distance < attackRange;
    }
}
