package io.github.StarvingValley.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.StarvingValley.controllers.JoystickController;
import io.github.StarvingValley.utils.ScreenUtils;

public class JoystickOverlay extends ScreenAdapter {
    private Stage stage;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private Vector2 joystickCenter = new Vector2(200, 200);
    private Vector2 knobPosition;
    private float joystickRadius = 120;
    private float knobRadius = 50;
    private JoystickController controller;
    private Color backgroundColor = new Color(0.2f, 0.2f, 0.2f, 0.3f);
    private Color knobColor = new Color(0.7f, 0.7f, 0.7f, 0.9f);
    private float minDragStartDistance = 250;
    private boolean isJoystickActive = false;

    public JoystickOverlay(JoystickController controller) {
        this.controller = controller;
        this.stage = new Stage(new ScreenViewport());
        this.knobPosition = new Vector2(joystickCenter);

        Gdx.input.setInputProcessor(inputAdapter);
    }

    private InputAdapter inputAdapter = new InputAdapter() {
        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            handleDrag(screenX, screenY);
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            resetJoystick();
            return true;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            isJoystickActive = touchIsWithinActiveRange(screenX, screenY);

            return true;
        }
    };

    public void handleDrag(int screenX, int screenY) {
        if (!isJoystickActive) {
            return;
        }

        Vector2 touchPos = ScreenUtils.convertTouchCoordinatesToRenderCoordinates(screenX, screenY);

        Vector2 delta = new Vector2(touchPos.x - joystickCenter.x, touchPos.y - joystickCenter.y);
        delta.setLength(Math.min(delta.len(), joystickRadius));

        float distance = delta.len();
        float dragAmount = distance / joystickRadius;

        knobPosition.set(joystickCenter.x + delta.x, joystickCenter.y + delta.y);

        Vector2 direction = delta.nor().scl(dragAmount);

        controller.handleJoystickDrag(direction);
    }

    public void resetJoystick() {
        knobPosition.set(joystickCenter);
        controller.resetJoystick();
    }

    private boolean touchIsWithinActiveRange(int screenX, int screenY) {
        boolean isValid = true;

        Vector2 posTouchDown = ScreenUtils.convertTouchCoordinatesToRenderCoordinates(screenX, screenY);

        if (posTouchDown.dst2(joystickCenter) > minDragStartDistance * minDragStartDistance) {
            isValid = false;
        }

        return isValid;
    }

    public void render() {
        stage.act();
        stage.draw();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(backgroundColor);
        shapeRenderer.circle(joystickCenter.x, joystickCenter.y, joystickRadius);
        shapeRenderer.setColor(knobColor);
        shapeRenderer.circle(knobPosition.x, knobPosition.y, knobRadius);
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
    }
}
