package io.github.StarvingValley.models.entities;

import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ActiveWorldEntityComponent;
import io.github.StarvingValley.models.components.AnimationComponent;
import io.github.StarvingValley.models.components.CameraFollowComponent;
import io.github.StarvingValley.models.components.CollidableComponent;
import io.github.StarvingValley.models.components.CurrentScreenComponent;
import io.github.StarvingValley.models.components.DamageComponent;
import io.github.StarvingValley.models.components.EconomyComponent;
import io.github.StarvingValley.models.components.HotbarComponent;
import io.github.StarvingValley.models.components.HungerComponent;
import io.github.StarvingValley.models.components.InputComponent;
import io.github.StarvingValley.models.components.InventoryComponent;
import io.github.StarvingValley.models.components.PlayerComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpeedComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.SyncComponent;
import io.github.StarvingValley.models.components.TileOccupierComponent;
import io.github.StarvingValley.models.components.VelocityComponent;
import io.github.StarvingValley.models.components.WorldLayerComponent;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.types.InventoryInfo;
import io.github.StarvingValley.models.types.InventoryType;
import io.github.StarvingValley.models.types.ItemStack;
import io.github.StarvingValley.models.types.PrefabType;
import io.github.StarvingValley.models.types.ScreenType;
import io.github.StarvingValley.models.types.WorldLayer;
import io.github.StarvingValley.utils.AnimationFactory;
import io.github.StarvingValley.utils.Assets;
import io.github.StarvingValley.utils.InventoryUtils;

public class PlayerFactory {
  public static Entity createPlayer(
      float x, float y, float width, float height, float speed, Assets assets, Entity camera) {

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
      entity.add(new InventoryComponent(new InventoryInfo(6, 3, InventoryType.INVENTORY)));
      entity.add(new HotbarComponent(new InventoryInfo(6, 1, InventoryType.HOTBAR)));
      entity.add(new SpriteComponent("DogBasic.png"));
      entity.add(new DamageComponent(Config.DEFAULT_DAMAGE_AMOUNT, Config.DEFAULT_ATTACK_RANGE, Config.DEFAULT_ATTACK_SPEED));
      entity.add(new CurrentScreenComponent(ScreenType.FARM));

      CameraFollowComponent cameraFollowComponent = new CameraFollowComponent(camera);
      cameraFollowComponent.targetCamera = camera;

    entity.add(cameraFollowComponent);

    AnimationComponent anim = AnimationFactory.createAnimationsForType(PrefabType.PLAYER, assets);
    entity.add(anim);

    return entity;
  }

  public static void initializePlayerInventory(Engine engine, Entity player, EventBus eventBus) {
    HotbarComponent hotbar = Mappers.hotbar.get(player);
    if (hotbar == null)
      return;

    InventoryUtils.initializeInventory(engine, hotbar.info, List.of(new ItemStack(PrefabType.SOIL, 4),
        new ItemStack(PrefabType.WHEAT_SEEDS,
            2),
        new ItemStack(PrefabType.BEETROOT_SEEDS,
            2)),
        eventBus);
  }
}
