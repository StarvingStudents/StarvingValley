package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;

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

        Rectangle nextPosXRect = new Rectangle(
                position.position.x + velocity.velocity.x, position.position.y, size.width, size.height);
        Rectangle nextPosYRect = new Rectangle(
                position.position.x, position.position.y + velocity.velocity.y, size.width, size.height);

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

            // TODO: Just check bottom of player when comparing with environment collidables

            if (nextPosXRect.overlaps(envCollidableRect)) {
                isBlockedX = true;
            }
            if (nextPosYRect.overlaps(envCollidableRect)) {
                isBlockedY = true;
            }
        }

        // TODO: Slide over small bumps
        if (isBlockedX)
            velocity.velocity.x = 0;
        if (isBlockedY)
            velocity.velocity.y = 0;
    }
}
