package io.github.StarvingValley.views;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.controllers.InputEventController;

public class InputEventAdapter extends InputAdapter {
    private final InputEventController controller;
    private Vector2 downPos = new Vector2();
    private boolean touchDownFired = false;
    private boolean dragStarted = false;
    private Vector2 lastTouchPos = new Vector2();
    private int lastButton = -1;

    public InputEventAdapter(InputEventController controller) {
        this.controller = controller;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        dragStarted = false;

        downPos.set(screenX, screenY);
        lastTouchPos.set(screenX, screenY);
        lastButton = button;
        touchDownFired = false;

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        lastTouchPos.set(screenX, screenY);
        lastButton = button;

        if (dragStarted) {
            controller.handleDragEnd(screenX, screenY, pointer, button);
        } else if (!touchDownFired && downPos.dst(lastTouchPos) <= Config.MAX_TAP_DISTANCE) {
            controller.handleTap(screenX, screenY, pointer, button);
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        lastTouchPos.set(screenX, screenY);

        if (!dragStarted) {
            float distance = downPos.dst(lastTouchPos);

            if (distance > Config.MAX_TAP_DISTANCE) {
                controller.handleDragStart(screenX, screenY, pointer, lastButton);
                dragStarted = true;
                touchDownFired = true;
            }
        }

        return false;
    }
}
