package io.github.StarvingValley.models.events;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.models.interfaces.Event;

public abstract class TileInputEvent implements Event {
    public final GridPoint2 tile;
    public final Vector2 screenPos;
    public final int button;

    public TileInputEvent(GridPoint2 tile, Vector2 screenPos, int button) {
        this.tile = tile;
        this.button = button;
        this.screenPos = screenPos;
    }
}
