package io.github.StarvingValley.models.events;

import com.badlogic.gdx.math.GridPoint2;

import io.github.StarvingValley.models.Interfaces.Event;

public abstract class TileInputEvent implements Event {
    public final GridPoint2 tile;
    public final int button;

    public TileInputEvent(GridPoint2 tile, int button) {
        this.tile = tile;
        this.button = button;
    }
}
