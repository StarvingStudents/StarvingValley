package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.CollidableComponent;
import io.github.StarvingValley.models.components.EnvironmentCollidableComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.VelocityComponent;

public class EnvironmentCollisionSystem extends IteratingSystem {
    public EnvironmentCollisionSystem() {
        super(Family
                .all(CollidableComponent.class, PositionComponent.class, SizeComponent.class, VelocityComponent.class)
                .get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = Mappers.position.get(entity);
        SizeComponent size = Mappers.size.get(entity);
        VelocityComponent velocity = Mappers.velocity.get(entity);

        // Allow some overlap without collisions on top part of collidables
        float boundsCollidableHeight = size.height * Config.BOUNDS_BOTTOM_COLLISION_RATIO;

        Rectangle futureBoundsX = new Rectangle(
                position.position.x + velocity.velocity.x, position.position.y, size.width, boundsCollidableHeight);
        Rectangle futureBoundsY = new Rectangle(
                position.position.x, position.position.y + velocity.velocity.y, size.width, boundsCollidableHeight);

        boolean isBlockedX = false;
        boolean isBlockedY = false;

        for (Entity envCollidable : getEngine().getEntitiesFor(Family
                .all(EnvironmentCollidableComponent.class, PositionComponent.class, SizeComponent.class)
                .get())) {

            if (entity == envCollidable)
                continue;

            PositionComponent envCollidablePos = Mappers.position.get(envCollidable);
            SizeComponent envCollidableSize = Mappers.size.get(envCollidable);

            Rectangle envCollidableRect = new Rectangle(envCollidablePos.position.x, envCollidablePos.position.y,
                    envCollidableSize.width,
                    envCollidableSize.height);

            if (futureBoundsX.overlaps(envCollidableRect)) {
                isBlockedX = true;
            }
            if (futureBoundsY.overlaps(envCollidableRect)) {
                isBlockedY = true;
            }
        }

        // Maybe add sliding over small bumps to make moving against uneven collision
        // objects more smooth
        if (isBlockedX)
            velocity.velocity.x = 0;
        if (isBlockedY)
            velocity.velocity.y = 0;
    }
}
