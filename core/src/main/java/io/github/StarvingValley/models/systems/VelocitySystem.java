package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.InputComponent;
import io.github.StarvingValley.models.components.SpeedComponent;
import io.github.StarvingValley.models.components.VelocityComponent;

public class VelocitySystem extends IteratingSystem {

    public VelocitySystem() {
        super(Family
                .all(InputComponent.class, VelocityComponent.class, SpeedComponent.class)
                .get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        VelocityComponent velocity = Mappers.velocity.get(entity);
        SpeedComponent speed = Mappers.speed.get(entity);
        InputComponent input = Mappers.input.get(entity);

        Vector2 direction = input.movingDirection.cpy();
        float speedMultiplier = direction.len();

        direction.nor();

        velocity.velocity.set(direction.scl(speed.speed * speedMultiplier * deltaTime));
    }
}
