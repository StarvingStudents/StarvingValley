package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.DropComponent;
import io.github.StarvingValley.models.components.PickupButtonComponent;
import io.github.StarvingValley.models.components.PickupComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.events.AddItemToInventoryEvent;
import io.github.StarvingValley.models.events.EntityRemovedEvent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.ItemStack;
import io.github.StarvingValley.models.types.PrefabType;

public class PickupSystem extends EntitySystem {
    private GameContext context;

    public PickupSystem(GameContext context) {
        this.context = context;
    }

    @Override
    public void update(float deltaTime) {
        if (context.player == null) {
            return;
        }

        // Check if pickup button was clicked
        ImmutableArray<Entity> clickedButtons = getEngine().getEntitiesFor(
            Family.all(PickupButtonComponent.class, ClickedComponent.class).get());

        if (clickedButtons.size() == 0) {
            return;
        }

        PositionComponent playerPos = Mappers.position.get(context.player);
        if (playerPos == null) {
            return;
        }

        // Get all pickupable entities
        ImmutableArray<Entity> pickupableEntities = getEngine().getEntitiesFor(
            Family.all(PickupComponent.class, PositionComponent.class).get());

        Entity closestEntity = null;
        float closestDistance = Float.MAX_VALUE;

        // Find closest pickupable entity
        for (Entity entity : pickupableEntities) {
            PositionComponent entityPos = Mappers.position.get(entity);
            PickupComponent pickup = Mappers.pickup.get(entity);

            float distance = Vector2.dst(
                playerPos.position.x, playerPos.position.y,
                entityPos.position.x, entityPos.position.y);

            if (distance <= pickup.pickupRange && distance < closestDistance) {
                closestDistance = distance;
                closestEntity = entity;
            }
        }

        // Process pickup if an entity is in range
        if (closestEntity != null) {
            DropComponent drop = Mappers.drop.get(closestEntity);
            if (drop != null && drop.drops != null) {
                // Add items to inventory
                for (ItemStack itemStack : drop.drops) {
                    context.eventBus.publish(new AddItemToInventoryEvent(context.player, itemStack.type, itemStack.quantity));
                }
            }

            // Remove the entity
            context.eventBus.publish(new EntityRemovedEvent(closestEntity));
            getEngine().removeEntity(closestEntity);
        }

        // Remove ClickedComponent from all clicked buttons
        for (Entity button : clickedButtons) {
            button.remove(ClickedComponent.class);
        }
    }
} 