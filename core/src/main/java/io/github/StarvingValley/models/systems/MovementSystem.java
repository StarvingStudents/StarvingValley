package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.TargetComponent;
import io.github.StarvingValley.models.components.VelocityComponent;

public class MovementSystem extends IteratingSystem {
    private static final float SPEED = 100f;

    public MovementSystem() {
        super(Family.all(PositionComponent.class, VelocityComponent.class, TargetComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = entity.getComponent(PositionComponent.class);
        VelocityComponent velocity = entity.getComponent(VelocityComponent.class);
        TargetComponent target = entity.getComponent(TargetComponent.class);

        if (target.target != null) {
            Vector2 direction = new Vector2(target.target).sub(position.position).nor();
            velocity.velocity.set(direction.scl(SPEED * deltaTime));

            if (position.position.dst(target.target) > 1) {
                position.position.add(velocity.velocity);
            }
        }
    }
}
