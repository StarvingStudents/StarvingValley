package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SpeedComponent;
import io.github.StarvingValley.models.components.VelocityComponent;
import io.github.StarvingValley.models.state.InputState;

public class MovementSystem extends IteratingSystem {
  public MovementSystem() {
    super(
        Family.all(PositionComponent.class, VelocityComponent.class, SpeedComponent.class)
            .get());
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    PositionComponent position = entity.getComponent(PositionComponent.class);
    VelocityComponent velocity = entity.getComponent(VelocityComponent.class);
    SpeedComponent speed = entity.getComponent(SpeedComponent.class);

    Vector2 direction = new Vector2(0, 0);

    if (InputState.isMovingRight)
      direction.x += 1;
    if (InputState.isMovingLeft)
      direction.x -= 1;
    if (InputState.isMovingUp)
      direction.y += 1;
    if (InputState.isMovingDown)
      direction.y -= 1;

    direction.nor();

    velocity.velocity.set(direction.scl(speed.speed * deltaTime));
    position.position.add(velocity.velocity);
  }
}
