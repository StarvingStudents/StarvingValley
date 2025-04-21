package io.github.StarvingValley.views;

import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import io.github.StarvingValley.controllers.FarmController;
import io.github.StarvingValley.controllers.GameMenuController;
import io.github.StarvingValley.controllers.InputEventController;
import io.github.StarvingValley.controllers.JoystickController;
import io.github.StarvingValley.controllers.StarvingValley;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.Interfaces.IFirebaseRepository;
import io.github.StarvingValley.models.components.CameraComponent;
import io.github.StarvingValley.models.components.HotbarComponent;
import io.github.StarvingValley.models.components.InventoryComponent;
import io.github.StarvingValley.models.entities.HudFactory;
import io.github.StarvingValley.models.entities.TraderFactory;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.events.InventoryCloseEvent;
import io.github.StarvingValley.models.events.InventoryOpenEvent;
import io.github.StarvingValley.models.events.NotificationEvent;
import io.github.StarvingValley.models.types.InventoryType;
import io.github.StarvingValley.models.types.ItemTrade;
import io.github.StarvingValley.models.types.PrefabType;
import io.github.StarvingValley.utils.BuildUtils;
import io.github.StarvingValley.utils.EventDebugger;
import io.github.StarvingValley.utils.InventoryUtils;

public class FarmView extends ScreenAdapter {
  private final EventDebugger eventDebugger;
  public AssetManager assetManager;
  IFirebaseRepository _firebaseRepository;
  private JoystickOverlay joystickOverlay;
  private InputAdapter inputAdapter; // temp
  private InputEventAdapter inputEventAdapter;
  private EventBus eventBus;
  private FarmController controller;
  private Engine engine;
  private EventDebugOverlay eventDebugOverlay;

  private NotificationOverlay notificationOverlay;

  private GameMenuController gameMenuController;

  public FarmView(StarvingValley game, IFirebaseRepository firebaseRepository) {
    _firebaseRepository = firebaseRepository;
    eventDebugger = new EventDebugger();
    eventDebugOverlay = new EventDebugOverlay(eventDebugger);
    eventBus = new EventBus(eventDebugger);

    notificationOverlay = new NotificationOverlay(eventBus);

    // pre-load some assets that we know we always need.
    // Potentially add assetManager.finishLoading(); to wait
    assetManager = new AssetManager();
    // assetManager.load("DogBasic.png", Texture.class);
    assetManager.load("tomato1.png", Texture.class);
    assetManager.load("potato1.png", Texture.class);
    assetManager.load("dirt.png", Texture.class);
    assetManager.load("inventory_slot.png", Texture.class);
    assetManager.load("inventory_slot_highlight.png", Texture.class);
    assetManager.load("inventory_open_button.png", Texture.class);

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
    assetManager.load("action_axe_down.png", Texture.class);
    assetManager.load("action_axe_up.png", Texture.class);
    assetManager.load("action_axe_left.png", Texture.class);
    assetManager.load("action_axe_right.png", Texture.class);

    assetManager.load("GameMenu.png", Texture.class);

    assetManager.finishLoading();

    controller = new FarmController(game, _firebaseRepository, eventBus, assetManager);

    engine = controller.getEngine();

    // TODO: Keeping this to make dev easier
    inputAdapter = new InputAdapter() {
      @Override
      public boolean keyDown(int keycode) {
        PrefabType prefabType = null;

        InventoryComponent inventory = Mappers.inventory.get(controller.getPlayer());

        switch (keycode) {
          case Input.Keys.C:
            prefabType = PrefabType.WHEAT_CROP;
            break;
          case Input.Keys.W:
            prefabType = PrefabType.WALL;
            break;
          case Input.Keys.E:
            prefabType = PrefabType.BEETROOT_CROP;
            break;
          case Input.Keys.F:
            prefabType = PrefabType.SOIL;
            break;
          case Input.Keys.I:
            if (inventory.info.isOpen) {
              eventBus.publish(new InventoryCloseEvent(inventory.info));
            } else {
              eventBus.publish(new InventoryOpenEvent(inventory.info));
            }
            break;
        }

        if (prefabType != null) {
          BuildUtils.enableBuildPreview(prefabType, prefabType, engine);
        }

        return true;
      }
    };

    gameMenuController = new GameMenuController(controller.getGameContext());

    CameraComponent cameraComponent = Mappers.camera.get(controller.getCamera());
    inputEventAdapter = new InputEventAdapter(new InputEventController(cameraComponent.camera, eventBus));
  }

  // TODO: Maybe this shouldn't be in the view, but then we need a way to either
  // know player is initalized inside farmcontroller, or add a playerloaded
  // callback
  boolean addedHotbar = false;

  private void showHotbar() {
    if (controller.getPlayer() == null)
      return;

    HotbarComponent hotbar = Mappers.hotbar.get(controller.getPlayer());
    InventoryComponent inventory = Mappers.inventory.get(controller.getPlayer());
    if (hotbar != null && !hotbar.info.isOpen && !addedHotbar) {
      hotbar.info.inventoryType = InventoryType.HOTBAR;
      eventBus.publish(new InventoryOpenEvent(hotbar.info));
      InventoryUtils.addInventoryToggleButtonToEngine(engine, hotbar.info, inventory.info);
      addedHotbar = true;
    }
  }

  @Override
  public void show() {
    JoystickController joystickController = new JoystickController(engine);
    joystickOverlay = new JoystickOverlay(joystickController);

    InputAdapter joystickInputAdapter = joystickOverlay.getInputAdapter();

    FilteringInputMultiplexer multiplexer = new FilteringInputMultiplexer(() -> gameMenuController.isVisible());
    multiplexer.addProcessor(inputEventAdapter);
    multiplexer.addProcessor(inputAdapter); // temp
    multiplexer.addProcessor(joystickInputAdapter);

    Gdx.input.setInputProcessor(multiplexer);

    // Temp until we have villageview
    engine.addEntity(HudFactory.createEatingButton());
    engine.addEntity(HudFactory.createFarmToWorldMapButton());
    engine.addEntity(HudFactory.createPickupButton());
    engine.addEntity(TraderFactory.create(30, 13, PrefabType.SOIL, 0));
    engine.addEntity(TraderFactory.create(32, 13, PrefabType.WHEAT_SEEDS, 0));
    engine.addEntity(TraderFactory.create(34, 13, PrefabType.BEETROOT_SEEDS, 0));

    eventBus.publish(new NotificationEvent("Press f to start farming mode."));
    eventBus.publish(new NotificationEvent("Press c to plant beetroots."));
    eventBus.publish(new NotificationEvent("Press e to plant wheat."));
  }

  @Override
  public void render(float delta) {
    assetManager.update();
    controller.update(delta);

    showHotbar();
    gameMenuController.update();

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
    gameMenuController.render();

    notificationOverlay.render();
    eventDebugOverlay.render();
  }

  @Override
  public void dispose() {
    controller.dispose();
  }
}
