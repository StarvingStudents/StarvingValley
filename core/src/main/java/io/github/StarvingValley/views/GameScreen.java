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
import io.github.StarvingValley.models.components.CameraComponent;
import io.github.StarvingValley.models.components.CameraFollowComponent;
import io.github.StarvingValley.models.entities.CameraFactory;
import io.github.StarvingValley.models.entities.MapFactory;
import io.github.StarvingValley.models.entities.PlayerFactory;
import io.github.StarvingValley.models.systems.CameraSystem;
import io.github.StarvingValley.models.systems.MapRenderSystem;
import io.github.StarvingValley.models.systems.MovementSystem;
import io.github.StarvingValley.models.systems.RenderSystem;
import io.github.StarvingValley.utils.MapUtils;

public class GameScreen extends ScreenAdapter {
  IFirebaseRepository _firebaseRepository;
  private Engine engine;
  private SpriteBatch batch;
  private Entity player;
  private Entity camera;
  private Entity map;
  private float unitScale;

  private JoystickOverlay joystickOverlay;

  public GameScreen(IFirebaseRepository firebaseRepository) {
    _firebaseRepository = firebaseRepository;
  }

  @Override
  public void show() {
    batch = new SpriteBatch();

    unitScale = 1 / Config.PIXELS_PER_TILE;

    int tilesWide = 14;
    int tilesHigh = MapUtils.calculateVerticalTileCount(tilesWide);

    camera = CameraFactory.createCamera(tilesWide, tilesHigh);

    CameraFollowComponent cameraFollowComponent = new CameraFollowComponent();
    cameraFollowComponent.targetCamera = camera;

    CameraComponent cameraComponent = camera.getComponent(CameraComponent.class);

    map = MapFactory.CreateMap("FarmMap.tmx", unitScale, cameraComponent);

    engine = new Engine();

    player = PlayerFactory.createPlayer(30, 15, 1, 1, 5f, "DogBasic.png");
    player.add(cameraFollowComponent);

    engine.addEntity(player);
    engine.addEntity(camera);
    engine.addEntity(map);
    engine.addSystem(new MapRenderSystem());
    engine.addSystem(new MovementSystem());
    engine.addSystem(new CameraSystem());
    engine.addSystem(new RenderSystem(batch));

    JoystickController joystickController = new JoystickController();
    joystickOverlay = new JoystickOverlay(joystickController);
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    Gdx.gl.glClearColor(0, 0, 0, 1);

    batch.begin();

    CameraComponent cameraComponent = camera.getComponent(CameraComponent.class);
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
