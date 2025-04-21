package io.github.StarvingValley.views;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;

import io.github.StarvingValley.controllers.InputEventController;
import io.github.StarvingValley.controllers.JoystickController;
import io.github.StarvingValley.controllers.StarvingValley;
import io.github.StarvingValley.controllers.VisitFarmController;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.CameraComponent;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.interfaces.PlayerDataRepository;
import io.github.StarvingValley.utils.EventDebugger;

public class VisitFarmView extends ScreenAdapter {
  private final EventDebugger eventDebugger;
  public AssetManager assetManager;
  PlayerDataRepository _firebaseRepository;
  private JoystickOverlay joystickOverlay;
  private InputEventAdapter inputEventAdapter;
  private EventBus eventBus;

  private Engine engine;
  private EventDebugOverlay eventDebugOverlay;
  private VisitFarmController controller;

  public VisitFarmView(StarvingValley game, PlayerDataRepository firebaseRepository, String userId) {
    _firebaseRepository = firebaseRepository;
    eventDebugger = new EventDebugger();
    eventDebugOverlay = new EventDebugOverlay(eventDebugger);
    eventBus = new EventBus(eventDebugger);
    assetManager = new AssetManager();

    controller = new VisitFarmController(game, userId, _firebaseRepository, eventBus, assetManager);

    engine = controller.getEngine();

    CameraComponent cameraComponent = Mappers.camera.get(controller.getCamera());
    inputEventAdapter =
        new InputEventAdapter(new InputEventController(cameraComponent.camera, eventBus));
  }

  @Override
  public void show() {

    JoystickController joystickController = new JoystickController(engine);
    joystickOverlay = new JoystickOverlay(joystickController);

    InputAdapter joystickInputAdapter = joystickOverlay.getInputAdapter();

    InputMultiplexer multiplexer = new InputMultiplexer();
    multiplexer.addProcessor(inputEventAdapter);
    multiplexer.addProcessor(joystickInputAdapter);

    Gdx.input.setInputProcessor(multiplexer);
  }

  @Override
  public void render(float delta) {
    assetManager.update();
    controller.update(delta);

    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    Gdx.gl.glEnable(GL20.GL_BLEND);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    CameraComponent cameraComponent = Mappers.camera.get(controller.getCamera());
    if (cameraComponent != null) {
      controller.getBatch().setProjectionMatrix(cameraComponent.camera.combined);
    }

    engine.update(delta);
    joystickOverlay.render();
    // eventDebugOverlay.render();
  }

  @Override
  public void dispose() {
    controller.dispose();
  }
}
