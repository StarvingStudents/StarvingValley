package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.models.components.ActiveWorldEntityComponent;
import io.github.StarvingValley.models.components.CameraFollowComponent;
import io.github.StarvingValley.models.components.CollidableComponent;
import io.github.StarvingValley.models.components.HungerComponent;
import io.github.StarvingValley.models.components.InputComponent;
import io.github.StarvingValley.models.components.PlayerComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpeedComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.SyncComponent;
import io.github.StarvingValley.models.components.TileOccupierComponent;
import io.github.StarvingValley.models.components.VelocityComponent;
import io.github.StarvingValley.models.components.WorldLayerComponent;
import io.github.StarvingValley.models.types.WorldLayer;

public class PlayerFactory {
  public static Entity createPlayer(
      float x, float y, float width, float height, float speed, String spritePath, Entity camera) {

    Entity entity = new Entity();

    entity.add(new PositionComponent(x, y, 100));
    entity.add(new SpeedComponent(speed));
    entity.add(new VelocityComponent(new Vector2()));
    entity.add(new SpriteComponent(spritePath));
    entity.add(new SizeComponent(width, height));
    entity.add(new CollidableComponent());
    entity.add(new HungerComponent());
    entity.add(new TileOccupierComponent());
    entity.add(new InputComponent());
    entity.add(new WorldLayerComponent(WorldLayer.CHARACTER));
    entity.add(new SyncComponent());
    entity.add(new PlayerComponent());
    entity.add(new ActiveWorldEntityComponent());

    CameraFollowComponent cameraFollowComponent = new CameraFollowComponent(camera);
    cameraFollowComponent.targetCamera = camera;

    entity.add(cameraFollowComponent);

    return entity;
  }
}
