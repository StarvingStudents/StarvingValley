package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.VelocityComponent;
import io.github.StarvingValley.models.events.EntityUpdatedEvent;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.utils.DiffUtils;
import io.github.StarvingValley.utils.TileUtils;

public class MovementSystem extends IteratingSystem {
  private EventBus eventBus;

  public MovementSystem(EventBus eventBus) {
    super(Family.all(PositionComponent.class, VelocityComponent.class).get());
    this.eventBus = eventBus;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    PositionComponent position = Mappers.position.get(entity);
    VelocityComponent velocity = Mappers.velocity.get(entity);

    Vector3 oldPosition = new Vector3(position.position);
    position.position.x += velocity.velocity.x;
    position.position.y += velocity.velocity.y;

    TileUtils.updateOverlappingTiles(entity);

    if (DiffUtils.hasChanged(position.position, oldPosition))
      eventBus.publish(new EntityUpdatedEvent(entity));
  }
}
