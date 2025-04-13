package io.github.StarvingValley.models.events;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

public class TouchDownEvent extends TileInputEvent {
    public TouchDownEvent(GridPoint2 tile, Vector2 screenPos, int button) {
        super(tile, screenPos, button);
    }
}
