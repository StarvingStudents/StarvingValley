package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

import io.github.StarvingValley.models.types.PrefabType;

public class BuildableComponent implements Component {
    public PrefabType builds;

    public BuildableComponent(PrefabType type) {
        this.builds = type;
    }
}
