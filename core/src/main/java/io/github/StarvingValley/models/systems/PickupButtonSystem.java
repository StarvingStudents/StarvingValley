package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.PickupButtonComponent;
import io.github.StarvingValley.models.components.PickupComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.types.GameContext;

public class PickupButtonSystem extends EntitySystem {
    private GameContext context;

    public PickupButtonSystem(GameContext context) {
        this.context = context;
    }

    @Override
    public void update(float deltaTime) {
        // Get all pickup buttons
        ImmutableArray<Entity> pickupButtons = getEngine().getEntitiesFor(
            Family.all(PickupButtonComponent.class, SpriteComponent.class).get());

        if (pickupButtons.size() == 0 || context.player == null) {
            return;
        }

        PositionComponent playerPos = Mappers.position.get(context.player);
        if (playerPos == null) {
            return;
        }

        Entity pickupButton = pickupButtons.first();
        SpriteComponent buttonSprite = Mappers.sprite.get(pickupButton);
        boolean hasPickupableInRange = false;

        // Get all pickupable entities
        ImmutableArray<Entity> pickupableEntities = getEngine().getEntitiesFor(
            Family.all(PickupComponent.class, PositionComponent.class).get());

        // Check for pickupable entities in range
        for (Entity entity : pickupableEntities) {
            PositionComponent entityPos = Mappers.position.get(entity);
            PickupComponent pickup = Mappers.pickup.get(entity);

            float distance = Vector2.dst(
                playerPos.position.x, playerPos.position.y,
                entityPos.position.x, entityPos.position.y);

            if (distance <= pickup.pickupRange) {
                hasPickupableInRange = true;
                break;
            }
        }

        // Set button color based on pickup availability
        buttonSprite.sprite.setColor(hasPickupableInRange ? 
            new Color(0.8f, 0.6f, 0.2f, 1f) : // Light brown
            new Color(0.5f, 0.5f, 0.5f, 1f));  // Grey

        // Handle button click
        ImmutableArray<Entity> clickedButtons = getEngine().getEntitiesFor(
            Family.all(PickupButtonComponent.class, ClickedComponent.class).get());

        if (clickedButtons.size() > 0) {
            for (Entity button : clickedButtons) {
                button.remove(ClickedComponent.class);
            }
        }
    }
} 