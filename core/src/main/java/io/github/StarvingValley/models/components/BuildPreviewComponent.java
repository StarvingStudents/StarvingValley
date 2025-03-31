package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

import io.github.StarvingValley.models.Interfaces.IBuildableEntityFactory;
import io.github.StarvingValley.models.types.WorldLayer;

public class BuildPreviewComponent implements Component {
    public boolean isBlocked = false;
    public IBuildableEntityFactory entityFactory;

    public BuildPreviewComponent(IBuildableEntityFactory entityFactory) {
        this.entityFactory = entityFactory;
    }

    public WorldLayer getPreviewLayer() {
        return entityFactory.getWorldLayer();
    }
}
