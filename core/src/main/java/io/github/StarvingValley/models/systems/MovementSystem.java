package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.VelocityComponent;

public class MovementSystem extends IteratingSystem {
  public MovementSystem() {
    super(
        Family.all(PositionComponent.class, VelocityComponent.class)
            .get());
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    PositionComponent position = entity.getComponent(PositionComponent.class);
    VelocityComponent velocity = entity.getComponent(VelocityComponent.class);
    position.position.add(velocity.velocity);
  }
}
