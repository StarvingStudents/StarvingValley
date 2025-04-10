package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

import io.github.StarvingValley.models.types.PrefabType;

public class PrefabTypeComponent implements Component {
    public PrefabType type;

    public PrefabTypeComponent(PrefabType type) {
        this.type = type;
    }
}
