package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.SpeedComponent;
import io.github.StarvingValley.models.components.VelocityComponent;
import io.github.StarvingValley.models.state.InputState;

public class VelocitySystem extends IteratingSystem {

    public VelocitySystem() {
        super(Family
                .all(VelocityComponent.class, SpeedComponent.class)
                .get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        VelocityComponent velocity = Mappers.velocity.get(entity);
        SpeedComponent speed = Mappers.speed.get(entity);

        Vector2 direction = InputState.movingDirection.cpy();
        float speedMultiplier = direction.len();

        direction.nor();

        velocity.velocity.set(direction.scl(speed.speed * speedMultiplier * deltaTime));
    }
}
