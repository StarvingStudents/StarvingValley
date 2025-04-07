package io.github.StarvingValley.views;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.controllers.InputEventController;

public class InputEventAdapter extends InputAdapter {
    private final InputEventController controller;

    public InputEventAdapter(InputEventController controller) {
        this.controller = controller;
    }

    private Vector2 downPos = new Vector2();
    private boolean touchDownFired = false;

    private boolean touchReleased = false;
    private Vector2 lastTouchPos = new Vector2();
    private int lastPointer = -1;
    private int lastButton = -1;

    private float touchDownDelay = Config.MAX_TAP_DURATION / 1000f;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchReleased = false;
        downPos.set(screenX, screenY);
        lastTouchPos.set(screenX, screenY);
        lastPointer = pointer;
        lastButton = button;
        touchDownFired = false;

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                propogateEvents();
            }
        }, touchDownDelay);

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touchReleased = true;
        lastTouchPos.set(screenX, screenY);
        lastPointer = pointer;
        lastButton = button;

        propogateEvents();

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        lastTouchPos.set(screenX, screenY);
        return false;
    }

    private boolean propogateEvents() {
        if (touchDownFired) {
            return false;
        }

        float distance = downPos.dst(lastTouchPos);

        if (touchReleased && distance <= Config.MAX_TAP_DISTANCE) {
            controller.handleTap((int) lastTouchPos.x, (int) lastTouchPos.y, lastPointer, lastButton);
        } else if (!touchReleased && distance > Config.MAX_TAP_DISTANCE) {
            controller.handleDragStart((int) lastTouchPos.x, (int) lastTouchPos.y, lastPointer, lastButton);
        } else {
            controller.handleTouchDown((int) lastTouchPos.x, (int) lastTouchPos.y, lastPointer, lastButton);
        }

        touchDownFired = true;

        return true;
    }
}
