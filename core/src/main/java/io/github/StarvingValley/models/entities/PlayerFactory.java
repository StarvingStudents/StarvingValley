package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.models.components.CollidableComponent;
import io.github.StarvingValley.models.components.HungerComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpeedComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.VelocityComponent;

public class PlayerFactory {
  public static Entity createPlayer(
      float x, float y, float width, float height, float speed, String spritePath) {

      Entity entity = new Entity();
      
      entity.add(new PositionComponent(x, y));
      entity.add(new SpeedComponent(speed));
      entity.add(new VelocityComponent(new Vector2()));
      entity.add(new SpriteComponent(spritePath));
      entity.add(new SizeComponent(width, height));
      entity.add(new CollidableComponent());
      entity.add(new HungerComponent()); 
      
      return entity;
    }
}
