package io.github.StarvingValley.models.events;

import com.badlogic.gdx.math.GridPoint2;

public class TouchDownEvent extends TileInputEvent {
    public TouchDownEvent(GridPoint2 tile, GridPoint2 screenTile, int button) {
        super(tile, screenTile, button);
    }
}
