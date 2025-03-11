package io.github.StarvingValley.views;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.StarvingValley.controllers.InputController;
import io.github.StarvingValley.models.entities.PlayerFactory;
import io.github.StarvingValley.models.systems.MovementSystem;
import io.github.StarvingValley.models.systems.RenderSystem;

public class GameScreen extends ScreenAdapter {
    private Engine engine;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Entity player;

    @Override
    public void show() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        engine = new Engine();

        player = PlayerFactory.createPlayer(400, 240);
        engine.addEntity(player);
        engine.addSystem(new MovementSystem());
        engine.addSystem(new RenderSystem(batch));

        Gdx.input.setInputProcessor(new InputController(player));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        engine.update(delta);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
