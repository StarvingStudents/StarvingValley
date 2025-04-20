package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
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

public class PickupSystem extends IteratingSystem {
    private GameContext context;

    public PickupSystem(GameContext context) {
        super(Family.all(PickupComponent.class, PositionComponent.class).get());
        this.context = context;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (!hasPickupButtonPressed()) {
            return;
        }

        if (context.player == null) {
            return;
        }
        PositionComponent playerPos = Mappers.position.get(context.player);
        if (playerPos == null) {
            return;
        }

        PositionComponent entityPos = Mappers.position.get(entity);
        PickupComponent pickup = Mappers.pickup.get(entity);

        // Calculate distance between player and entity
        float distance = Vector2.dst(
            playerPos.position.x, playerPos.position.y,
            entityPos.position.x, entityPos.position.y
        );

        if (distance <= pickup.pickupRange) {
            DropComponent drops = Mappers.drop.get(entity);
            if (drops != null && drops.drops != null) {
                for (ItemStack drop : drops.drops) {
                    context.eventBus.publish(new AddItemToInventoryEvent(drop));
                }
            }

            context.eventBus.publish(new EntityRemovedEvent(entity));
            getEngine().removeEntity(entity);
        }
    }

    private boolean hasPickupButtonPressed() {
        return !context.eventBus.getEvents(PickupButtonPressedEvent.class).isEmpty();
    }
} 