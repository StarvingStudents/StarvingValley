package io.github.StarvingValley.views;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;

import io.github.StarvingValley.controllers.GameMenuController;
import io.github.StarvingValley.controllers.InputEventController;
import io.github.StarvingValley.controllers.StarvingValley;
import io.github.StarvingValley.controllers.WorldMapController;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.CameraComponent;
import io.github.StarvingValley.models.entities.HudFactory;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.interfaces.PlayerDataRepository;
import io.github.StarvingValley.utils.EventDebugger;

public class WorldMapView extends ScreenAdapter {
  private final EventDebugger eventDebugger;
  public AssetManager assetManager;
  PlayerDataRepository _firebaseRepository;
  private InputEventAdapter inputEventAdapter;
  private EventBus eventBus;
  private WorldMapController controller;
  private Engine engine;
  private EventDebugOverlay eventDebugOverlay;
  private GameMenuController gameMenuController;

  public WorldMapView(StarvingValley game, PlayerDataRepository firebaseRepository) {

    _firebaseRepository = firebaseRepository;
    eventDebugger = new EventDebugger();
    eventDebugOverlay = new EventDebugOverlay(eventDebugger);
    eventBus = new EventBus(eventDebugger);

    assetManager = new AssetManager();

    controller = new WorldMapController(game, _firebaseRepository, eventBus, assetManager);
    engine = controller.getEngine();

    gameMenuController = new GameMenuController(controller.gameContext);

    CameraComponent cameraComponent = Mappers.camera.get(controller.getCamera());
    inputEventAdapter = new InputEventAdapter(new InputEventController(cameraComponent.camera, eventBus));
  }

  @Override
  public void show() {

    FilteringInputMultiplexer multiplexer = new FilteringInputMultiplexer(() -> gameMenuController.isVisible());
    multiplexer.addProcessor(inputEventAdapter);

    Gdx.input.setInputProcessor(multiplexer);

    engine.addEntity(HudFactory.createWorldMapToFarmButton());
  }

  @Override
  public void render(float delta) {
    assetManager.update();

    controller.update(delta);
    gameMenuController.update();

    Gdx.gl.glClearColor(.753f, .831f, .439f, 0);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    Gdx.gl.glEnable(GL20.GL_BLEND);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    CameraComponent cameraComponent = Mappers.camera.get(controller.getCamera());
    if (cameraComponent != null) {
      controller.getBatch().setProjectionMatrix(cameraComponent.camera.combined);
    }

    engine.update(delta);
    gameMenuController.render();
    // eventDebugOverlay.render();
  }

  @Override
  public void dispose() {
    controller.dispose();
  }
}
