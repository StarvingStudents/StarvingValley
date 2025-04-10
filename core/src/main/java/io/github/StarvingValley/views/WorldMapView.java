package io.github.StarvingValley.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.controllers.InputEventController;
import io.github.StarvingValley.controllers.JoystickController;
import io.github.StarvingValley.controllers.WorldMapController;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.Interfaces.AuthCallback;
import io.github.StarvingValley.models.Interfaces.EntityDataCallback;
import io.github.StarvingValley.models.Interfaces.IFirebaseRepository;
import io.github.StarvingValley.models.Interfaces.UserIdsCallback;
import io.github.StarvingValley.models.components.CameraComponent;
import io.github.StarvingValley.models.components.UnsyncedComponent;
import io.github.StarvingValley.models.dto.SyncEntity;
import io.github.StarvingValley.models.entities.PlayerFactory;
import io.github.StarvingValley.models.entities.WorldMapUserFactory;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.types.PrefabType;
import io.github.StarvingValley.utils.BuildUtils;
import io.github.StarvingValley.utils.EntitySerializer;
import io.github.StarvingValley.utils.EventDebugger;
import io.github.StarvingValley.utils.MapUtils;

public class WorldMapView extends ScreenAdapter {
  public AssetManager assetManager;
  IFirebaseRepository _firebaseRepository;
  private JoystickOverlay joystickOverlay;
  private InputAdapter inputAdapter; // temp
  private InputEventAdapter inputEventAdapter;

  private EventBus eventBus;
  private WorldMapController controller;
  private Engine engine;

  private final EventDebugger eventDebugger;
  private EventDebugOverlay eventDebugOverlay;

  public WorldMapView(IFirebaseRepository firebaseRepository) {
    _firebaseRepository = firebaseRepository;
    eventDebugger = new EventDebugger();
    eventDebugOverlay = new EventDebugOverlay(eventDebugger);
    eventBus = new EventBus(eventDebugger);

    // pre-load some assets that we know we always need.
    // Potentially add assetManager.finishLoading(); to wait
    assetManager = new AssetManager();
    assetManager.load("DogBasic.png", Texture.class);
    assetManager.load("Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\BlueHouse.png",
        Texture.class);
    assetManager.load("Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\GreenHouse.png",
        Texture.class);
    assetManager.load("Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\House.png",
        Texture.class);
    assetManager.load("Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\OrangeHouse.png",
        Texture.class);
    assetManager.load("Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\PinkHouse.png",
        Texture.class);
    assetManager.load("Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\PurpleHouse1.png",
        Texture.class);

    controller = new WorldMapController(_firebaseRepository, eventBus, assetManager); // initializing this here to avoid
                                                                                      // problems with the temporal
                                                                                      // input handling
    engine = controller.getEngine();

    CameraComponent cameraComponent = Mappers.camera.get(controller.getCamera());
    inputEventAdapter = new InputEventAdapter(new InputEventController(cameraComponent.camera, eventBus));
  }

  @Override
  public void show() {

    InputMultiplexer multiplexer = new InputMultiplexer();
    multiplexer.addProcessor(inputEventAdapter);

    Gdx.input.setInputProcessor(multiplexer);

    controller.loadUserEntities(_firebaseRepository);

  }

  @Override
  public void render(float delta) {

    assetManager.update();

    controller.update(delta);

    Gdx.gl.glClearColor(.753f, .831f, .439f, 0);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    Gdx.gl.glEnable(GL20.GL_BLEND);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    CameraComponent cameraComponent = Mappers.camera.get(controller.getCamera());
    if (cameraComponent != null) {
      controller.getBatch().setProjectionMatrix(cameraComponent.camera.combined);
    }

    engine.update(delta);
    eventDebugOverlay.render();
  }

  @Override
  public void dispose() {
    controller.dispose();
  }
}
