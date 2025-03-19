package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;

import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpeedComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.VelocityComponent;

public class PlayerFactory {
  public static Entity createPlayer(
      float x, float y, float width, float height, float speed, String spritePath) {

      Entity entity = new Entity();
      PositionComponent position = new PositionComponent();
      position.position.set(x, y);

      SpeedComponent speedComponent = new SpeedComponent();
      speedComponent.speed = speed;

      entity.add(position);
      entity.add(speedComponent);
      entity.add(new VelocityComponent());
      entity.add(new SpriteComponent(spritePath));
      entity.add(new SizeComponent(width, height));
      return entity;
    }
}
