package io.github.StarvingValley.views;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.controllers.JoystickController;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.Interfaces.IFirebaseRepository;
import io.github.StarvingValley.models.components.CameraComponent;
import io.github.StarvingValley.models.components.CameraFollowComponent;
import io.github.StarvingValley.models.components.TiledMapComponent;
import io.github.StarvingValley.models.entities.CameraFactory;
import io.github.StarvingValley.models.entities.MapFactory;
import io.github.StarvingValley.models.entities.PlayerFactory;
import io.github.StarvingValley.models.systems.CameraSystem;
import io.github.StarvingValley.models.systems.DurabilityRenderSystem;
import io.github.StarvingValley.models.systems.EnvironmentCollisionSystem;
import io.github.StarvingValley.models.systems.HungerRenderSystem;
import io.github.StarvingValley.models.systems.HungerSystem;
import io.github.StarvingValley.models.systems.MapRenderSystem;
import io.github.StarvingValley.models.systems.MovementSystem;
import io.github.StarvingValley.models.systems.RenderSystem;
import io.github.StarvingValley.models.systems.VelocitySystem;
import io.github.StarvingValley.utils.MapUtils;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.StarvingValley.models.systems.InventorySystem;

public class GameScreen extends ScreenAdapter {
  IFirebaseRepository _firebaseRepository;
  private Engine engine;
  private SpriteBatch batch;
  private Entity player;
  private Entity camera;
  private Entity map;
  private InventorySystem inventorySystem;

  private JoystickOverlay joystickOverlay;

    private boolean isInventoryVisible = false;
    private InventoryOverlay inventoryOverlay;
    private Viewport viewport; // Add a viewport

  public GameScreen(IFirebaseRepository firebaseRepository) {
    _firebaseRepository = firebaseRepository;
      viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Initialize viewport
      inventoryOverlay = new InventoryOverlay(viewport);
  }

  @Override
  public void show() {
    batch = new SpriteBatch();

    int tilesWide = 14;
    int tilesHigh = MapUtils.calculateVerticalTileCount(tilesWide);

    camera = CameraFactory.createCamera(tilesWide, tilesHigh);

    CameraFollowComponent cameraFollowComponent = new CameraFollowComponent();
    cameraFollowComponent.targetCamera = camera;

    CameraComponent cameraComponent = Mappers.camera.get(camera);

    map = MapFactory.CreateMap("FarmMap.tmx", Config.UNIT_SCALE, cameraComponent);

    engine = new Engine();

    player = PlayerFactory.createPlayer(35, 15, 1, 1, 5f, "DogBasic.png");
    player.add(cameraFollowComponent);

    engine.addEntity(player);
    engine.addEntity(camera);
    engine.addEntity(map);
    engine.addSystem(new MapRenderSystem());
    engine.addSystem(new VelocitySystem());
    engine.addSystem(new EnvironmentCollisionSystem());
    engine.addSystem(new MovementSystem());
    engine.addSystem(new CameraSystem());
    engine.addSystem(new RenderSystem(batch));
    engine.addSystem(new DurabilityRenderSystem(engine, batch));
    engine.addSystem(new HungerSystem());
    engine.addSystem(new HungerRenderSystem(engine, batch));

    inventorySystem = new InventorySystem();
    engine.addSystem(new InventorySystem());

    JoystickController joystickController = new JoystickController();
    joystickOverlay = new JoystickOverlay(joystickController);

    TiledMapComponent tiledMap = Mappers.tiledMap.get(map);
    MapUtils.loadCollidables(tiledMap.tiledMap, Config.UNIT_SCALE, engine);
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    Gdx.gl.glClearColor(0, 0, 0, 1);

      // Toggle inventory visibility
      if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
          isInventoryVisible = !isInventoryVisible;
      }

    batch.begin();

    CameraComponent cameraComponent = Mappers.camera.get(camera);
    if (cameraComponent != null) {
      batch.setProjectionMatrix(cameraComponent.camera.combined);
    }

    engine.update(delta);
    batch.end();

    joystickOverlay.render();
      // Render inventory overlay if visible
      if (isInventoryVisible) {
          inventoryOverlay.render(inventorySystem, player);
      }
  }

    @Override
    public void resize(int width, int height) {
      viewport.update(width, height, true);
    }

  @Override
  public void dispose() {
    batch.dispose();
  }
}
