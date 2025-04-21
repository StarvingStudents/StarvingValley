package io.github.StarvingValley.controllers;

import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.models.entities.CameraFactory;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.events.ScreenTransitionEvent;
import io.github.StarvingValley.models.events.WorldMapFarmClickEvent;
import io.github.StarvingValley.models.interfaces.AuthCallback;
import io.github.StarvingValley.models.interfaces.PlayerDataRepository;
import io.github.StarvingValley.models.interfaces.UserIdsCallback;
import io.github.StarvingValley.models.systems.CameraSystem;
import io.github.StarvingValley.models.systems.EventCleanupSystem;
import io.github.StarvingValley.models.systems.FirebaseSyncSystem;
import io.github.StarvingValley.models.systems.HUDButtonPressHandlingSystem;
import io.github.StarvingValley.models.systems.HUDButtonPressSystem;
import io.github.StarvingValley.models.systems.HudRenderSystem;
import io.github.StarvingValley.models.systems.InputCleanupSystem;
import io.github.StarvingValley.models.systems.InputSystem;
import io.github.StarvingValley.models.systems.RenderSystem;
import io.github.StarvingValley.models.systems.SpriteSystem;
import io.github.StarvingValley.models.systems.SyncMarkingSystem;
import io.github.StarvingValley.models.systems.WorldMapTransitionSystem;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.ScreenType;
import io.github.StarvingValley.utils.AnimationUtils;
import io.github.StarvingValley.utils.Assets;
import io.github.StarvingValley.utils.MapUtils;
import io.github.StarvingValley.views.VisitFarmView;

public class WorldMapController {

  private final Engine engine;
  private final SpriteBatch batch;
  private final EventBus eventBus;
  private final PlayerDataRepository firebaseRepository;
  private final StarvingValley game;
  public GameContext gameContext;
  private Entity camera;

  public WorldMapController(
      StarvingValley game,
      PlayerDataRepository firebaseRepository,
          EventBus eventBus,
      AssetManager assetManager) {
    this.firebaseRepository = firebaseRepository;
    this.eventBus = eventBus;
    this.engine = new Engine();
    this.batch = new SpriteBatch();
    this.game = game;

    gameContext = new GameContext(eventBus, batch, assetManager, firebaseRepository, engine, new Assets(assetManager));

    AnimationUtils.loadTexturesForAnimation(assetManager);
    initGame();
  }

  private void initGame() {
    int tilesWide = Config.CAMERA_TILES_WIDE;
    float tilesHigh = MapUtils.calculateVerticalTileCount(tilesWide);

    camera = CameraFactory.createCamera(tilesWide, tilesHigh);

    engine.addEntity(camera);

    engine.addSystem(new InputSystem(gameContext));
    engine.addSystem(new WorldMapTransitionSystem(gameContext));
    engine.addSystem(new CameraSystem());
    engine.addSystem(new RenderSystem(gameContext));
    engine.addSystem(new SpriteSystem(gameContext));
    engine.addSystem(new HudRenderSystem());
    engine.addSystem(new HUDButtonPressSystem(gameContext));
    engine.addSystem(new HUDButtonPressHandlingSystem(gameContext));
    engine.addSystem(new SyncMarkingSystem(gameContext));
    engine.addSystem(new FirebaseSyncSystem(gameContext));
    engine.addSystem(new InputCleanupSystem());
    engine.addSystem(new EventCleanupSystem(gameContext));

    loadUserEntities();
  }

  public void update(float deltaTime) {

    List<WorldMapFarmClickEvent> events = eventBus.getEvents(WorldMapFarmClickEvent.class);
    if (events.size() > 0) {
      String targetUserId = events.get(0).userId;
      System.out.println(targetUserId);
      game.switchView(new VisitFarmView(game, firebaseRepository, targetUserId));
    }

    List<ScreenTransitionEvent> screenTransitionEvents = eventBus.getEvents(ScreenTransitionEvent.class);
    if (screenTransitionEvents.isEmpty())
      return;

    if (screenTransitionEvents.get(0).getTargetScreen() == ScreenType.FARM) {
      game.requestViewSwitch(ScreenType.FARM);
    }
  }

  public void loadUserEntities() {
    firebaseRepository.registerOrSignInWithDeviceId(
        new AuthCallback() {
          @Override
          public void onSuccess() {
            firebaseRepository.getAllUserIds(
                new UserIdsCallback() {
                  @Override
                  public void onSuccess(List<String> data) {
                    data.remove(firebaseRepository.getCurrentUserId());
                    MapUtils.loadWorldMapFarmEntities(data, engine);
                  }

                  @Override
                  public void onFailure(String errorMessage) {
                    System.err.println("Failed to load entities: " + errorMessage);
                  }
                });
          }

          @Override
          public void onFailure(String errorMessage) {
            // TODO: Fail gracefully
            throw new RuntimeException("Authentication failed: " + errorMessage);
          }
        });
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
