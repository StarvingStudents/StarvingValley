package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class DraggingComponent implements Component {
    public final int originScreenX;
    public final int originScreenY;

    public final int originWorldTileX;
    public final int originWorldTileY;

    public DraggingComponent(int screenX, int screenY, int worldTileX, int worldTileY) {
        this.originScreenX = screenX;
        this.originScreenY = screenY;
        this.originWorldTileX = worldTileX;
        this.originWorldTileY = worldTileY;
    }
}
