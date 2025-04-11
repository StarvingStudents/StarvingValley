package io.github.StarvingValley.views;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import io.github.StarvingValley.controllers.FarmController;
import io.github.StarvingValley.controllers.InputEventController;
import io.github.StarvingValley.controllers.InventoryController;
import io.github.StarvingValley.controllers.JoystickController;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.Interfaces.IFirebaseRepository;
import io.github.StarvingValley.models.components.CameraComponent;
import io.github.StarvingValley.models.entities.TraderFactory;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.events.InventoryCloseEvent;
import io.github.StarvingValley.models.events.InventoryOpenEvent;
import io.github.StarvingValley.models.types.PrefabType;
import io.github.StarvingValley.utils.BuildUtils;
import io.github.StarvingValley.utils.EventDebugger;

public class FarmView extends ScreenAdapter {
  public AssetManager assetManager;
  IFirebaseRepository _firebaseRepository;
  private JoystickOverlay joystickOverlay;
  private InputAdapter inputAdapter; //temp
  private InputEventAdapter inputEventAdapter;

  private EventBus eventBus;
  private FarmController controller;
  private Engine engine;

  private final EventDebugger eventDebugger;
  private EventDebugOverlay eventDebugOverlay;

  private InventoryController inventoryController;

  public FarmView(IFirebaseRepository firebaseRepository) {
    _firebaseRepository = firebaseRepository;
    eventDebugger = new EventDebugger();
    eventDebugOverlay = new EventDebugOverlay(eventDebugger);
    eventBus = new EventBus(eventDebugger);

    // pre-load some assets that we know we always need.
    // Potentially add assetManager.finishLoading(); to wait
    assetManager = new AssetManager();
    // assetManager.load("DogBasic.png", Texture.class);
    assetManager.load("dirt.png", Texture.class);
    assetManager.load("inventory_slot.png", Texture.class);

    assetManager.load("idle_down.png", Texture.class);
    assetManager.load("idle_up.png", Texture.class);
    assetManager.load("idle_left.png", Texture.class);
    assetManager.load("idle_right.png", Texture.class);
    assetManager.load("walking_down.png", Texture.class);
    assetManager.load("walking_up.png", Texture.class);
    assetManager.load("walking_left.png", Texture.class);
    assetManager.load("walking_right.png", Texture.class);
    assetManager.load("action_soil_down.png", Texture.class);
    assetManager.load("action_soil_up.png", Texture.class);
    assetManager.load("action_soil_left.png", Texture.class);
    assetManager.load("action_soil_right.png", Texture.class);
    assetManager.finishLoading();

    // initializing this here to avoid
    // problems with the temporal input
    // handling
    controller = new FarmController(_firebaseRepository, eventBus, assetManager);

    engine = controller.getEngine();

    inventoryController = new InventoryController(controller.getGameContext());
    inventoryController.setHotbarIsVisible(true);

      // TODO: Temp logic. When inventory is implemented it should handle this, and it
    // should only be possible on entities
    // with BuildableComponent. Use BuildUtils.isBuildable
    inputAdapter =
        new InputAdapter() {
          @Override
          public boolean keyDown(int keycode) {
            PrefabType prefabType = null;

            switch (keycode) {
              case Input.Keys.C:
                prefabType = PrefabType.WHEAT_CROP;
                break;
              case Input.Keys.E:
                prefabType = PrefabType.BEETROOT_CROP;
                break;
              case Input.Keys.F:
                prefabType = PrefabType.SOIL;
                break;
              case Input.Keys.I:
                if (inventoryController.isInventoryIsVisible()) {
                  eventBus.publish(new InventoryCloseEvent());
                } else {
                  eventBus.publish(new InventoryOpenEvent(controller.getPlayer()));
                }
            }

            if (prefabType != null) {
              BuildUtils.toggleBuildPreview(prefabType, engine);
            }

            return true;
          }
        };

      CameraComponent cameraComponent = Mappers.camera.get(controller.getCamera());
      inputEventAdapter = new InputEventAdapter(new InputEventController(cameraComponent.camera, eventBus));
  }

  @Override
  public void show() {

    JoystickController joystickController = new JoystickController(engine);
    joystickOverlay = new JoystickOverlay(joystickController);

    InputAdapter joystickInputAdapter = joystickOverlay.getInputAdapter();

    InputMultiplexer multiplexer = new InputMultiplexer();
    multiplexer.addProcessor(inputEventAdapter);
    multiplexer.addProcessor(inputAdapter); //temp
    multiplexer.addProcessor(joystickInputAdapter);

    Gdx.input.setInputProcessor(multiplexer);

    // Temp until we have villageview
    engine.addEntity(TraderFactory.create(30, 13, PrefabType.SOIL, 0));
    engine.addEntity(TraderFactory.create(32, 13, PrefabType.WHEAT_SEEDS, 0));
    engine.addEntity(TraderFactory.create(34, 13, PrefabType.BEETROOT_SEEDS, 0));
  }

  @Override
  public void render(float delta) {
    assetManager.update();
    inventoryController.update();

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
    eventDebugOverlay.render();
    inventoryController.render();
  }

  @Override
  public void dispose() {
    controller.dispose();
  }
}
