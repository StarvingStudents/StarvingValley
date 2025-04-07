package io.github.StarvingValley.controllers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.GridPoint2;

import io.github.StarvingValley.models.events.DragStartEvent;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.events.TapEvent;
import io.github.StarvingValley.models.events.TouchDownEvent;
import io.github.StarvingValley.utils.TileUtils;

public class InputEventController {
    private final OrthographicCamera camera;
    private final EventBus eventBus;

    public InputEventController(OrthographicCamera camera, EventBus eventBus) {
        this.camera = camera;
        this.eventBus = eventBus;
    }

    public void handleTouchDown(int screenX, int screenY, int pointer, int button) {
        GridPoint2 tile = TileUtils.coordinatesToTile(screenX, screenY, camera);
        eventBus.publish(new TouchDownEvent(tile, button));
    }

    public void handleTap(int screenX, int screenY, int pointer, int button) {
        GridPoint2 tile = TileUtils.coordinatesToTile(screenX, screenY, camera);
        eventBus.publish(new TapEvent(tile, button));
    }

    public void handleDragStart(int screenX, int screenY, int pointer, int button) {
        GridPoint2 tile = TileUtils.coordinatesToTile(screenX, screenY, camera);
        eventBus.publish(new DragStartEvent(tile, button));
    }
}
