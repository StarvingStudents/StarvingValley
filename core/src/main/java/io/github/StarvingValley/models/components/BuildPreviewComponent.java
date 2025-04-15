package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

import io.github.StarvingValley.models.types.PrefabType;

public class BuildPreviewComponent implements Component {
    public boolean isBlocked = false;
    public PrefabType madeFromPrefabType;

    public BuildPreviewComponent(PrefabType madeFromPrefabType) {
        this.madeFromPrefabType = madeFromPrefabType;
    }
}
