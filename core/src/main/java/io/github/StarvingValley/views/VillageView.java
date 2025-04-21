package io.github.StarvingValley.views;

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

import io.github.StarvingValley.controllers.InputEventController;
import io.github.StarvingValley.controllers.JoystickController;
import io.github.StarvingValley.controllers.StarvingValley;
import io.github.StarvingValley.controllers.VillageController;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.Interfaces.IFirebaseRepository;
import io.github.StarvingValley.models.components.CameraComponent;
import io.github.StarvingValley.models.components.HotbarComponent;
import io.github.StarvingValley.models.components.InventoryComponent;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.events.InventoryOpenEvent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.InventoryType;
import io.github.StarvingValley.models.types.PrefabType;
import io.github.StarvingValley.utils.BuildUtils;
import io.github.StarvingValley.utils.EventDebugger;
import io.github.StarvingValley.utils.InventoryUtils;
import io.github.StarvingValley.utils.MapUtils;

public class VillageView extends ScreenAdapter {
    public AssetManager assetManager;
    IFirebaseRepository _firebaseRepository;
    private JoystickOverlay joystickOverlay;
    private InputEventAdapter inputEventAdapter;

    private EventBus eventBus;
    private VillageController controller;
    private Engine engine;

    private final EventDebugger eventDebugger;
    private EventDebugOverlay eventDebugOverlay;

    public VillageView(StarvingValley game, IFirebaseRepository firebaseRepository) {
        _firebaseRepository = firebaseRepository;
        eventDebugger = new EventDebugger();
        eventDebugOverlay = new EventDebugOverlay(eventDebugger);
        eventBus = new EventBus(eventDebugger);

        // pre-load some assets that we know we always need.
        // Potentially add assetManager.finishLoading(); to wait
        assetManager = new AssetManager();
        assetManager.load("DogBasic.png", Texture.class);
        assetManager.load("tomato1.png", Texture.class);
        assetManager.load("potato1.png", Texture.class);
        assetManager.load("dirt.png", Texture.class);
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

        controller = new VillageController(game, _firebaseRepository, eventBus, assetManager);
        // to avoid
        // problems with the temporal
        // input handling
        engine = controller.getEngine();

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
        multiplexer.addProcessor(joystickInputAdapter);

        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        assetManager.update();
        controller.update(delta);
        showHotbar();

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

    private boolean addedHotbar = false;

    private void showHotbar() {
        Entity player = controller.getPlayer();
        if (player == null) return;

        HotbarComponent hotbar = Mappers.hotbar.get(player);
        InventoryComponent inventory = Mappers.inventory.get(player);

        if (hotbar != null && !hotbar.info.isOpen && !addedHotbar) {
            hotbar.info.inventoryType = InventoryType.HOTBAR;
            eventBus.publish(new InventoryOpenEvent(hotbar.info));
            InventoryUtils.addInventoryToggleButtonToEngine(engine, hotbar.info, inventory.info);
            addedHotbar = true;
        }
    }

    @Override
    public void dispose() {
        controller.dispose();
    }
}
