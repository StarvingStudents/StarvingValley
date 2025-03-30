package io.github.StarvingValley.views;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.controllers.JoystickController;
import io.github.StarvingValley.models.Interfaces.IFirebaseRepository;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.CameraComponent;
import io.github.StarvingValley.models.components.CameraFollowComponent;
import io.github.StarvingValley.models.components.CropTypeComponent;
import io.github.StarvingValley.models.components.TiledMapComponent;
import io.github.StarvingValley.models.entities.CameraFactory;
import io.github.StarvingValley.models.entities.CropFactory;
import io.github.StarvingValley.models.entities.MapFactory;
import io.github.StarvingValley.models.entities.PlayerFactory;
import io.github.StarvingValley.models.systems.CameraSystem;
import io.github.StarvingValley.models.systems.CropGrowthSystem;
import io.github.StarvingValley.models.systems.DurabilityRenderSystem;
import io.github.StarvingValley.models.systems.EnvironmentCollisionSystem;
import io.github.StarvingValley.models.systems.HarvestingSystem;
import io.github.StarvingValley.models.systems.HungerRenderSystem;
import io.github.StarvingValley.models.systems.HungerSystem;
import io.github.StarvingValley.models.systems.MapRenderSystem;
import io.github.StarvingValley.models.systems.MovementSystem;
import io.github.StarvingValley.models.systems.RenderSystem;
import io.github.StarvingValley.models.systems.VelocitySystem;
import io.github.StarvingValley.utils.MapUtils;

public class GameScreen extends ScreenAdapter {
  IFirebaseRepository _firebaseRepository;
  private Engine engine;
  private SpriteBatch batch;
  private Entity player;
  private Entity camera;
  private Entity map;
  private Entity tomato;
  private Entity tomato2;
  private Entity potato;

  private JoystickOverlay joystickOverlay;

  public GameScreen(IFirebaseRepository firebaseRepository) {
    _firebaseRepository = firebaseRepository;
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
    tomato = CropFactory.createCrop(33, 17, CropTypeComponent.CropType.TOMATO);
    tomato2 = CropFactory.createCrop(33, 16, CropTypeComponent.CropType.TOMATO);
    potato = CropFactory.createCrop(33, 15, CropTypeComponent.CropType.POTATO);

    engine.addEntity(player);
    engine.addEntity(camera);
    engine.addEntity(map);
    engine.addEntity(tomato);
    engine.addEntity(tomato2);
    engine.addEntity(potato);
    engine.addSystem(new MapRenderSystem());
    engine.addSystem(new VelocitySystem());
    engine.addSystem(new EnvironmentCollisionSystem());
    engine.addSystem(new MovementSystem());
    engine.addSystem(new CameraSystem());
    engine.addSystem(new RenderSystem(batch));
    engine.addSystem(new DurabilityRenderSystem(engine, batch));
    engine.addSystem(new HungerSystem());
    engine.addSystem(new HungerRenderSystem(engine, batch));
    engine.addSystem(new CropGrowthSystem());
    engine.addSystem(new HarvestingSystem(player));

    JoystickController joystickController = new JoystickController();
    joystickOverlay = new JoystickOverlay(joystickController);

    TiledMapComponent tiledMap = Mappers.tiledMap.get(map);
    MapUtils.loadCollidables(tiledMap.tiledMap, Config.UNIT_SCALE, engine);
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    Gdx.gl.glClearColor(0, 0, 0, 1);

    batch.begin();

    CameraComponent cameraComponent = Mappers.camera.get(camera);
    if (cameraComponent != null) {
      batch.setProjectionMatrix(cameraComponent.camera.combined);
    }

    engine.update(delta);
    batch.end();

    joystickOverlay.render();
  }

  @Override
  public void dispose() {
    batch.dispose();
  }
}
