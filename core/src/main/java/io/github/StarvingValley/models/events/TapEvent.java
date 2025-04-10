package io.github.StarvingValley.models.events;

import com.badlogic.gdx.math.GridPoint2;

public class TapEvent extends TileInputEvent {
    public TapEvent(GridPoint2 tile, int button) {
        super(tile, button);
    }
}
