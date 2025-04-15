package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class DragEndComponent implements Component {
    public final int dropScreenX;
    public final int dropScreenY;

    public final int dropWorldTileX;
    public final int dropWorldTileY;

    public DragEndComponent(int screenX, int screenY, int worldTileX, int worldTileY) {
        this.dropScreenX = screenX;
        this.dropScreenY = screenY;
        this.dropWorldTileX = worldTileX;
        this.dropWorldTileY = worldTileY;
    }
}
