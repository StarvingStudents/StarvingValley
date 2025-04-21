package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.DropComponent;
import io.github.StarvingValley.models.components.PickupComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.events.AddItemToInventoryEvent;
import io.github.StarvingValley.models.events.EntityRemovedEvent;
import io.github.StarvingValley.models.events.PickupButtonPressedEvent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.ItemStack;

public class PickupSystem extends EntitySystem {
    private GameContext context;

    public PickupSystem(GameContext context) {
        this.context = context;
    }

    @Override
    public void update(float deltaTime) {
        if (!hasPickupButtonPressed()) {
            return;
        }

        // Get all pickupable entities
        ImmutableArray<Entity> pickupableEntities = getEngine().getEntitiesFor(
            Family.all(PickupComponent.class, PositionComponent.class).get());

        // Find the closest pickupable entity
        Entity closestEntity = null;
        float closestDistance = Float.MAX_VALUE;

        if (context.player == null) {
            return;
        }

        PositionComponent playerPos = Mappers.position.get(context.player);
        if (playerPos == null) {
            return;
        }

        for (Entity entity : pickupableEntities) {
            PositionComponent entityPos = Mappers.position.get(entity);
            PickupComponent pickup = Mappers.pickup.get(entity);

            float distance = Vector2.dst(
                playerPos.position.x, playerPos.position.y,
                entityPos.position.x, entityPos.position.y);

            if (distance <= pickup.pickupRange && distance < closestDistance) {
                closestEntity = entity;
                closestDistance = distance;
            }
        }

        // If we found a pickupable entity, pick it up
        if (closestEntity != null) {
            DropComponent drops = Mappers.drop.get(closestEntity);
            if (drops != null && drops.drops != null) {
                for (ItemStack drop : drops.drops) {
                    context.eventBus.publish(new AddItemToInventoryEvent(context.player, drop.type, drop.quantity));
                }
            }

            context.eventBus.publish(new EntityRemovedEvent(closestEntity));
            getEngine().removeEntity(closestEntity);
        }
    }

    private boolean hasPickupButtonPressed() {
        return !context.eventBus.getEvents(PickupButtonPressedEvent.class).isEmpty();
    }
} 