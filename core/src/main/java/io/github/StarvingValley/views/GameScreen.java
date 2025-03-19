package io.github.StarvingValley.views;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.StarvingValley.controllers.JoystickController;
import io.github.StarvingValley.models.Interfaces.IFirebaseRepository;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.entities.PlayerFactory;
import io.github.StarvingValley.models.systems.MovementSystem;
import io.github.StarvingValley.models.systems.RenderSystem;

public class GameScreen extends ScreenAdapter {
  IFirebaseRepository _firebaseRepository;
  private Engine engine;
  private SpriteBatch batch;
  private OrthographicCamera camera;
  private Entity player;
  private Texture backgroundTexture;

  private JoystickOverlay joystickOverlay;

  public GameScreen(IFirebaseRepository firebaseRepository) {
    _firebaseRepository = firebaseRepository;
  }

  private void updateCamera() {
    PositionComponent position = player.getComponent(PositionComponent.class);

    if (position != null) {
      camera.position.set(position.position.x, position.position.y, 0f);

      camera.update();
    }
  }

  @Override
  public void show() {
    batch = new SpriteBatch();
    camera = new OrthographicCamera();
    camera.setToOrtho(false, 800, 480);

    engine = new Engine();

    player = PlayerFactory.createPlayer(500, 500, 84, 100, "FarmerFrog.png");
    engine.addEntity(player);
    engine.addSystem(new MovementSystem());
    engine.addSystem(new RenderSystem(batch));

    JoystickController joystickController = new JoystickController();
    joystickOverlay = new JoystickOverlay(joystickController);

    backgroundTexture = new Texture("TempBackground.gif");
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    Gdx.gl.glClearColor(0, 0, 0, 1);

    updateCamera();

    batch.setProjectionMatrix(camera.combined);
    batch.begin();

    batch.setProjectionMatrix(camera.combined);
    batch.draw(backgroundTexture, 0, 0, 2000, 1000);

    engine.update(delta);
    batch.end();

    joystickOverlay.render();
  }

  @Override
  public void dispose() {
    batch.dispose();
  }
}
