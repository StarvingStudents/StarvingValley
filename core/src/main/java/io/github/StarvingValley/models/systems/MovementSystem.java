package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.VelocityComponent;
import io.github.StarvingValley.utils.SyncUtils;
import io.github.StarvingValley.utils.TileUtils;

public class MovementSystem extends IteratingSystem {
  public MovementSystem() {
    super(Family.all(PositionComponent.class, VelocityComponent.class).get());
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    PositionComponent position = Mappers.position.get(entity);
    VelocityComponent velocity = Mappers.velocity.get(entity);

    Vector3 oldPosition = new Vector3(position.position);
    position.position.x += velocity.velocity.x;
    position.position.y += velocity.velocity.y;

    TileUtils.updateOverlappingTiles(entity);

    SyncUtils.markUnsyncedIfChanged(entity, position.position, oldPosition);
  }
}
