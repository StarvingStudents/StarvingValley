package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.CameraComponent;
import io.github.StarvingValley.models.components.DurabilityComponent;
import io.github.StarvingValley.models.components.PlayerComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.events.EntityDamagedEvent;
import io.github.StarvingValley.models.types.GameContext;

public class DamageSystem extends EntitySystem {
    private final GameContext context;
    private final float damageAmount = 10f;
    private final float maxDamageDistance = 100.03f; // Same as harvesting system

     // Cooldown variables
     private float lastDamageTime = 0f;
     private final float damageCooldown = 0.5f;

    public DamageSystem(GameContext context) {
        this.context = context;
    }

    @Override
    public void update(float deltaTime) {
        // Update cooldown timer
        lastDamageTime += deltaTime;

        // Get player entity
        ImmutableArray<Entity> players = getEngine().getEntitiesFor(Family.all(PlayerComponent.class).get());
        if (players.size() == 0) {
            return;
        }
        Entity player = players.first();
        PositionComponent playerPos = Mappers.position.get(player);

        ImmutableArray<Entity> damageableEntities = getEngine().getEntitiesFor(
            Family.all(
                DurabilityComponent.class,
                PositionComponent.class,
                SizeComponent.class)
            .get());

        Entity cameraEntity = getEngine().getEntitiesFor(Family.all(CameraComponent.class).get()).first();
        if (cameraEntity == null) {
            return;
        }

        CameraComponent cameraComponent = Mappers.camera.get(cameraEntity);
        if (cameraComponent == null) {
            return;
        }

        OrthographicCamera camera = cameraComponent.camera;

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && lastDamageTime >= damageCooldown) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.input.getY();

            Vector3 worldCoordinates = camera.unproject(new Vector3(mouseX, mouseY, 0));

            for (Entity entity : damageableEntities) {
                PositionComponent position = Mappers.position.get(entity);
                SizeComponent size = Mappers.size.get(entity);
                DurabilityComponent durability = Mappers.durability.get(entity);

                if (isClickOnEntity(worldCoordinates.x, worldCoordinates.y, position, size) &&
                    isPlayerNearEntity(playerPos, position)) {
                    // Apply damage using the component's method
                    durability.reduceHealth(damageAmount);

                    // Trigger axe animation
                    context.eventBus.publish(new EntityDamagedEvent(entity));

                    // Reset cooldown
                    lastDamageTime = 0f;
                    break;
                }
            }
        }
    }

    private boolean isClickOnEntity(float mouseX, float mouseY, PositionComponent position, SizeComponent size) {
        // Check if click is within the entity's bounds
        return mouseX >= position.position.x - size.width/2 &&
               mouseX <= position.position.x + size.width/2 &&
               mouseY >= position.position.y - size.height/2 &&
               mouseY <= position.position.y + size.height/2;
    }

    private boolean isPlayerNearEntity(PositionComponent playerPos, PositionComponent entityPos) {
        float distance = playerPos.position.dst(entityPos.position);
        return distance < maxDamageDistance;
    }
}
