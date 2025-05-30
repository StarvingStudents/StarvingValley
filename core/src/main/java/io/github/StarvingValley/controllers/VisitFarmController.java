package io.github.StarvingValley.controllers;

import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.CameraComponent;
import io.github.StarvingValley.models.components.TiledMapComponent;
import io.github.StarvingValley.models.entities.CameraFactory;
import io.github.StarvingValley.models.entities.HudFactory;
import io.github.StarvingValley.models.entities.MapFactory;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.events.ScreenTransitionEvent;
import io.github.StarvingValley.models.interfaces.PlayerDataRepository;
import io.github.StarvingValley.models.systems.ActionAnimationSystem;
import io.github.StarvingValley.models.systems.AlphaPulseSystem;
import io.github.StarvingValley.models.systems.AnimationSystem;
import io.github.StarvingValley.models.systems.AttackTimerSystem;
import io.github.StarvingValley.models.systems.CameraSystem;
import io.github.StarvingValley.models.systems.DamageSystem;
import io.github.StarvingValley.models.systems.DurabilityRenderSystem;
import io.github.StarvingValley.models.systems.EnvironmentCollisionSystem;
import io.github.StarvingValley.models.systems.EventCleanupSystem;
import io.github.StarvingValley.models.systems.FirebaseSyncSystem;
import io.github.StarvingValley.models.systems.HUDButtonPressHandlingSystem;
import io.github.StarvingValley.models.systems.HUDButtonPressSystem;
import io.github.StarvingValley.models.systems.HudRenderSystem;
import io.github.StarvingValley.models.systems.InputCleanupSystem;
import io.github.StarvingValley.models.systems.InputSystem;
import io.github.StarvingValley.models.systems.MapRenderSystem;
import io.github.StarvingValley.models.systems.MovementSystem;
import io.github.StarvingValley.models.systems.RenderSystem;
import io.github.StarvingValley.models.systems.SpriteSystem;
import io.github.StarvingValley.models.systems.StealingSystem;
import io.github.StarvingValley.models.systems.SyncMarkingSystem;
import io.github.StarvingValley.models.systems.VelocitySystem;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.utils.AnimationUtils;
import io.github.StarvingValley.utils.Assets;
import io.github.StarvingValley.utils.MapUtils;

public class VisitFarmController {
  private final Engine engine;
  private final SpriteBatch batch;
  private final EventBus eventBus;
  private final String visitedUserId;
  public GameContext gameContext;
  private Entity camera;
  private Entity map;
  private StarvingValley game;

  public VisitFarmController(
      StarvingValley game,
      String visitedUserId,
      PlayerDataRepository firebaseRepository,
          EventBus eventBus,
      AssetManager assetManager) {
    this.visitedUserId = visitedUserId;
    this.eventBus = eventBus;
    this.engine = new Engine();
    this.batch = new SpriteBatch();
    this.game = game;

    gameContext = new GameContext(eventBus, batch, assetManager, firebaseRepository, engine, new Assets(assetManager));
    
    AnimationUtils.loadTexturesForAnimation(assetManager);
    initVisitModeGame();
  }

  private void initVisitModeGame() {
    int tilesWide = Config.CAMERA_TILES_WIDE;
    float tilesHigh = MapUtils.calculateVerticalTileCount(tilesWide);
    camera = CameraFactory.createCamera(tilesWide, tilesHigh);
    CameraComponent cameraComponent = Mappers.camera.get(camera);
    gameContext.camera = cameraComponent.camera;

    map = MapFactory.createMap("FarmMap.tmx", Config.UNIT_SCALE, cameraComponent);
    engine.addEntity(camera);
    engine.addEntity(map);
    engine.addSystem(new InputSystem(gameContext));
    engine.addSystem(new MapRenderSystem());
    engine.addSystem(new AlphaPulseSystem());
    engine.addSystem(new VelocitySystem());
    engine.addSystem(new AnimationSystem());
    engine.addSystem(new EnvironmentCollisionSystem());
    engine.addSystem(new MovementSystem(gameContext));
    engine.addSystem(new DurabilityRenderSystem(gameContext));
    engine.addSystem(new DamageSystem(gameContext));
    engine.addSystem(new AttackTimerSystem(gameContext));
    engine.addSystem(new CameraSystem());
    engine.addSystem(new StealingSystem(gameContext));
    engine.addSystem(new RenderSystem(gameContext));
    engine.addSystem(new SpriteSystem(gameContext));
    engine.addSystem(new DurabilityRenderSystem(gameContext));
    engine.addSystem(new HUDButtonPressSystem(gameContext));
    engine.addSystem(new HUDButtonPressHandlingSystem(gameContext));
    engine.addSystem(new HudRenderSystem());
    engine.addSystem(new SyncMarkingSystem(gameContext));
    engine.addSystem(new FirebaseSyncSystem(gameContext));
    engine.addSystem(new InputCleanupSystem());
    engine.addSystem(new EventCleanupSystem(gameContext));
    engine.addSystem(new ActionAnimationSystem(gameContext));

    TiledMapComponent tiledMap = Mappers.tiledMap.get(map);
    MapUtils.loadEnvCollidables(tiledMap.tiledMap, Config.UNIT_SCALE, engine);

    MapUtils.loadSyncedEntitiesForUser(gameContext, camera, visitedUserId);
    MapUtils.loadPlayerForAttack(gameContext, camera);
    engine.addEntity(HudFactory.createEconomyBar(gameContext));
    engine.addEntity(HudFactory.createAttackTimerDisplay(gameContext));
  }

  public void update(float deltaTime) {
    List<ScreenTransitionEvent> events = eventBus.getEvents(ScreenTransitionEvent.class);
    if (events.isEmpty())
      return;

    ScreenTransitionEvent event = events.get(0);
    game.requestViewSwitch(event.getTargetScreen());
  }

  public Engine getEngine() {
    return engine;
  }

  public SpriteBatch getBatch() {
    return batch;
  }

  public Entity getCamera() {
    return camera;
  }

  public void dispose() {
    batch.dispose();
  }
}
