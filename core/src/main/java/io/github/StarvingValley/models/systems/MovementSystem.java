package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.VelocityComponent;
import io.github.StarvingValley.models.events.EntityUpdatedEvent;
import io.github.StarvingValley.models.types.GameContext;

public class MovementSystem extends IteratingSystem {
  private GameContext context;

  public MovementSystem(GameContext context) {
    super(Family.all(PositionComponent.class, VelocityComponent.class).get());
    this.context = context;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    PositionComponent position = Mappers.position.get(entity);
    VelocityComponent velocity = Mappers.velocity.get(entity);

    Vector3 oldPosition = new Vector3(position.position);
    position.position.x += velocity.velocity.x;
    position.position.y += velocity.velocity.y;

    if (!position.position.epsilonEquals(oldPosition, 0.0001f))
      context.eventBus.publish(new EntityUpdatedEvent(entity));
  }
}
