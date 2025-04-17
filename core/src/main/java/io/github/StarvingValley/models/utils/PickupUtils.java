package io.github.StarvingValley.models.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.PickupComponent;
import io.github.StarvingValley.models.components.PositionComponent;

public class PickupUtils {
    /**
     * Checks if the player is within pickup range of an item.
     * @param player The player entity
     * @param item The item entity
     * @return true if the player is within pickup range, false otherwise
     */
    public static boolean isWithinPickupRange(Entity player, Entity item) {
        PositionComponent playerPos = Mappers.position.get(player);
        PositionComponent itemPos = Mappers.position.get(item);
        PickupComponent pickup = Mappers.pickup.get(item);

        if (playerPos == null || itemPos == null || pickup == null) {
            return false;
        }

        float dx = playerPos.position.x - itemPos.position.x;
        float dy = playerPos.position.y - itemPos.position.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        return distance <= pickup.pickupRange;
    }

    /**
     * Finds all pickup-able entities within range of the player.
     * @param engine The Ashley engine
     * @param player The player entity
     * @return An array of pickup-able entities within range
     */
    public static ImmutableArray<Entity> findPickupableEntitiesInRange(com.badlogic.ashley.core.Engine engine, Entity player) {
        ImmutableArray<Entity> pickupableEntities = engine.getEntitiesFor(
            Family.all(PickupComponent.class, PositionComponent.class).get()
        );

        return pickupableEntities;
    }

    /**
     * Removes an entity from the engine and cleans up its components.
     * @param engine The Ashley engine
     * @param entity The entity to remove
     */
    public static void removeEntity(com.badlogic.ashley.core.Engine engine, Entity entity) {
        if (entity != null) {
            engine.removeEntity(entity);
        }
    }
} 