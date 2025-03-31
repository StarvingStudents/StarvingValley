package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

import io.github.StarvingValley.models.types.WorldLayer;

public class WorldLayerComponent implements Component {
    public WorldLayer layer;

    public WorldLayerComponent(WorldLayer layer) {
        this.layer = layer;
    }
}
