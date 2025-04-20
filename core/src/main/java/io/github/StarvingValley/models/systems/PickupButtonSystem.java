package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ButtonComponent;
import io.github.StarvingValley.models.components.PickupComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.types.ButtonType;
import io.github.StarvingValley.models.types.GameContext;

public class PickupButtonSystem extends IteratingSystem {
    private GameContext context;

    public PickupButtonSystem(GameContext context) {
        super(Family.all(ButtonComponent.class, SpriteComponent.class).get());
        this.context = context;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ButtonComponent button = Mappers.button.get(entity);
        SpriteComponent sprite = Mappers.sprite.get(entity);

        if (button.buttonType != ButtonType.PICKUP_BUTTON) {
            return;
        }

        // Check if player is within pickup range of any pickupable entity
        boolean canPickup = isPlayerInPickupRange();

        // Set button color based on pickup availability
        if (canPickup) {
            sprite.sprite.setColor(new Color(0.8f, 0.6f, 0.2f, 1f)); // Light brown
        } else {
            sprite.sprite.setColor(new Color(0.5f, 0.5f, 0.5f, 1f)); // Grey
        }
    }

    private boolean isPlayerInPickupRange() {
        if (context.player == null) {
            return false;
        }

        PositionComponent playerPos = Mappers.position.get(context.player);
        if (playerPos == null) {
            return false;
        }

        ImmutableArray<Entity> pickupableEntities = getEngine().getEntitiesFor(
                Family.all(PickupComponent.class, PositionComponent.class).get());

        for (Entity entity : pickupableEntities) {
            PositionComponent entityPos = Mappers.position.get(entity);
            PickupComponent pickup = Mappers.pickup.get(entity);

            float distance = Vector2.dst(
                    playerPos.position.x, playerPos.position.y,
                    entityPos.position.x, entityPos.position.y);

            if (distance <= pickup.pickupRange) {
                return true;
            }
        }

        return false;
    }
} 