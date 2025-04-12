package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;

import io.github.StarvingValley.models.components.BuildableComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.types.PrefabType;

public class SeedFactory {
    public static Entity create(PrefabType type, PrefabType builds) {
        Entity seeds = new Entity();

        seeds.add(new SpriteComponent(type.getIconName()));
        seeds.add(new BuildableComponent(builds));

        return seeds;
    }
}
