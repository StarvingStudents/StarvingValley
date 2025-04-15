package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.models.components.ActiveWorldEntityComponent;
import io.github.StarvingValley.models.components.AnimationComponent;
import io.github.StarvingValley.models.components.CameraFollowComponent;
import io.github.StarvingValley.models.components.CollidableComponent;
import io.github.StarvingValley.models.components.DamageComponent;
import io.github.StarvingValley.models.components.CurrentScreenComponent;
import io.github.StarvingValley.models.components.EconomyComponent;
import io.github.StarvingValley.models.components.HotbarComponent;
import io.github.StarvingValley.models.components.HungerComponent;
import io.github.StarvingValley.models.components.InputComponent;
import io.github.StarvingValley.models.components.InventoryComponent;
import io.github.StarvingValley.models.components.PlayerComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpeedComponent;
import io.github.StarvingValley.models.components.SyncComponent;
import io.github.StarvingValley.models.components.TileOccupierComponent;
import io.github.StarvingValley.models.components.VelocityComponent;
import io.github.StarvingValley.models.components.WorldLayerComponent;
import io.github.StarvingValley.models.types.PrefabType;
import io.github.StarvingValley.models.types.ScreenType;
import io.github.StarvingValley.models.types.WorldLayer;
import io.github.StarvingValley.utils.AnimationFactory;


public class PlayerFactory {
  public static Entity createPlayer(
      float x, float y, float width, float height, float speed, AssetManager assetManager, Entity camera) {

      Entity entity = new Entity();

      entity.add(new PositionComponent(x, y, 100));
      entity.add(new SpeedComponent(speed));
      entity.add(new VelocityComponent(new Vector2()));
      entity.add(new SizeComponent(width, height));
      entity.add(new CollidableComponent());
      entity.add(new HungerComponent());
      entity.add(new TileOccupierComponent());
      entity.add(new InputComponent());
      entity.add(new WorldLayerComponent(WorldLayer.CHARACTER));
      entity.add(new SyncComponent());
      entity.add(new PlayerComponent());
      entity.add(new ActiveWorldEntityComponent());
      entity.add(new EconomyComponent(Config.STARTING_BALANCE));
      entity.add(new InventoryComponent());
      entity.add(new HotbarComponent());
      entity.add(new DamageComponent(Config.DEFAULT_DAMAGE_AMOUNT, Config.DEFAULT_ATTACK_RANGE, Config.DEFAULT_ATTACK_SPEED));
      entity.add(new CurrentScreenComponent(ScreenType.FARM));

      CameraFollowComponent cameraFollowComponent = new CameraFollowComponent(camera);
      cameraFollowComponent.targetCamera = camera;

      entity.add(cameraFollowComponent);

      AnimationComponent anim = AnimationFactory.createAnimationsForType(PrefabType.PLAYER, assetManager);
      entity.add(anim);


      return entity;
    }
}
